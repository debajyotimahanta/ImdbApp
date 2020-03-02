CREATE TABLE titles (
    tconst varchar(10),
    titleType text,
    primaryTitle text,
    originalTitle text,
    isAdult boolean,
    startYear int,
    endYear int default NULL,
    runtimeMinutes int,
    genres text[],
    PRIMARY KEY (tconst)
);

CREATE TABLE ratings(
  tconst varchar(10) PRIMARY KEY,
  averageRating real,
  numVotes int
);

CREATE TABLE episodes(
  tconst varchar(10) PRIMARY KEY,
  parentTconst varchar(10),
  seasonNumber int,
  episodeNumber int
);