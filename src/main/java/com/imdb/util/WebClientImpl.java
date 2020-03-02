package com.imdb.util;

import com.imdb.dao.ImdbFile;
import com.imdb.dao.impl.ImdbFileBuilder;
import com.imdb.dao.mapper.ImdbMapper;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class WebClientImpl implements WebClient {

    private int connectionTimeout;
    private int readTimeout;
    private boolean cacheFile;

    /**
     * @param connectionTimeout the number of milliseconds until this method
     *                          will timeout if no connection could be established to the <code>source</code>
     * @param readTimeout       the number of milliseconds until this method will
     * @param cacheFile         if file already exists we wont download it. Used for testing
     */
    public WebClientImpl(int connectionTimeout, int readTimeout, boolean cacheFile) {
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.cacheFile = cacheFile;
    }

    @Override
    public ImdbFile download(String url, ImdbMapper mapper) throws IOException {
        URL sourceUrl = new URL(url);
        File gZIPFile = new File("data/" + sourceUrl.getFile());
        if (!gZIPFile.exists()) FileUtils.copyURLToFile(sourceUrl, gZIPFile, connectionTimeout, readTimeout);
        GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(gZIPFile));
        Reader unzipedFile = new InputStreamReader(gzipInputStream, StandardCharsets.UTF_8);
        return new ImdbFileBuilder()
                .mapper(mapper)
                .reader(unzipedFile)
                .build();
    }
}
