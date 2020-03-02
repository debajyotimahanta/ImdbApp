# Assumptions
* We are using JPA to represent the IMDB movie Model. All the DAO are one to one mapping with the IMDB tables. However, we creating few relationship tables to maintain one too many relationships
* To load the data, we are using a Queue based pattern, where the request to upload is queued, and workers work on it. This approach is taken so that in the future, we can parallelize it.
* One of the requirements was only to import 2017 movie data. I did try to find the best way to do that. Initially, I thought the title file was sorted based on the year, and I thought I could scan through that get the titles for a particular date range. However, later I found thats, not the case. So, for now, i am importing everything. In addition, all the other tables like names, principles, rating didn't have any way to filter by year. We could have to maintain a list of a map of all the moves from 2017 and then do a scan of all associated items. However I think assuming the scope of this exercise was to understand java coding and best practices i didn't think it made sense to implement a distributed application that would break this task down and achieve it in a scalable way,


# Code Design
I am using a SpringBoot application and based on the parameter passed we execute the following tasks
* ImportAll
* Update Rating
* GetRating

### ImdbFile
This file represents the IMDB offline entity. Its the uzip file stored in memory, which is used the DAO to stream it and create entries in the data store. SInce we are using JPA and Hibernate, the data store can be changed easily. For this exercise, i am using SQLLite. 

### DAO
For data access, we are using JPA and Hiberate. We are using Spring to generate all the necessary CURD classes. Finally, for import, we are using ImdbFile calls the appropriate method in ImdbEntityDAO. These methods are guarded by transactions, so you are either able to import and fail.
Also in the ImdbEntityDAO implementation, we are flush and clear the cache to avoid excessive memory consumption.

### Mapper
This class is used to Map the CSV values over to the ImdbEntity. This mapper is simple and handles most of the translation.

### Testing
Testing is broken down into unit tests and integration test. For integration tests we are using in-memory database to test the import process end to end with fake data and file created in run time

### Run
I was able to run this with all the files except `title.principals.tsv.gz` because this file is 300M ziped and the import took over an hour.