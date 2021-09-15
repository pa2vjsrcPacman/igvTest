package org.broad.igv.bigwig;

import org.apache.log4j.Logger;
import org.broad.igv.data.BasicScore;
import org.broad.igv.data.DataSource;
import org.broad.igv.feature.LocusScore;
import org.broad.igv.track.TrackType;
import org.broad.igv.track.WindowFunction;
import org.broad.igv.util.HttpUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.broad.igv.data.AbstractDataSource.ORDERED_WINDOW_FUNCTIONS;

public class D4ServerSource implements DataSource {

    private static Logger log = Logger.getLogger(DataSource.class);

    String url;
    double min = 0;
    double max = 0.001;

    public D4ServerSource(String url) {
        this.url = url;
    }

    @Override
    public double getDataMax() {
        return min;
    }

    @Override
    public double getDataMin() {
        return max;
    }

    @Override
    public List<LocusScore> getSummaryScoresForRange(String chr, int start, int end, int zoom) {
        try {
            String queryURL = this.url.replace("d4get://", "http://") + "?chr=" + chr + "&start=" + start + "&end=" + end;
            byte[] bytes = HttpUtils.getInstance().getContentsAsBytes(new URL(queryURL), null);
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            int dataStart = byteBuffer.getInt();
            int stepSize = byteBuffer.getInt();
            int nPoints = byteBuffer.getInt();
            List<LocusScore> scores = new ArrayList<>(nPoints);
            for (int i = 0; i < nPoints; i++) {
                float value = byteBuffer.getFloat();
                scores.add(new BasicScore(dataStart, dataStart + stepSize, value));
                dataStart += stepSize;
                min = Math.min(min, value);
                max = Math.max(max, value);

            }
            return scores;
        } catch (IOException e) {
            log.error("Error reading from D4 server", e);
            return null;
        }
    }

    @Override
    public TrackType getTrackType() {
        return null;
    }

    @Override
    public void setWindowFunction(WindowFunction statType) {

    }

    @Override
    public boolean isLogNormalized() {
        return false;
    }

    @Override
    public WindowFunction getWindowFunction() {
        return null;
    }

//    @Override
//    public Collection<WindowFunction> getAvailableWindowFunctions() {
//        return ORDERED_WINDOW_FUNCTIONS;
//    }

    @Override
    public void dispose() {

    }
}
