package mis.kuas.chart;

import android.content.Context;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import mis.kuas.data.AccDataGetter;

public class AccRealTimeChart {

    private int chartLength = 50;
    private int count = chartLength;
    private AccDataGetter dataGetter;
    private XYSeries seriesX;
    private XYSeries seriesY;
    private XYSeries seriesZ;
    private XYMultipleSeriesDataset dataset;
    private XYMultipleSeriesRenderer renderer;
    private GraphicalView chart;
    private String title;

    public AccRealTimeChart(Context context, AccDataGetter dataGetter, String title) {
        this(context, title);
        this.dataGetter = dataGetter;
    }

    public AccRealTimeChart(Context context, String title) {
        this.title = title;
        buildDataset();
        setChartSettings();
        this.chart = ChartFactory.getLineChartView(context, dataset, renderer);
        this.chart.setBackgroundColor(Color.BLACK);

    }

    public void setDataGetter(AccDataGetter dataGetter) {
        this.dataGetter = dataGetter;
    }

    private void buildDataset() {
        seriesX = getfilledSeries("X");
        seriesY = getfilledSeries("Y");
        seriesZ = getfilledSeries("Z");
        dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(seriesX);
        dataset.addSeries(seriesY);
        dataset.addSeries(seriesZ);
    }

    private XYSeries getfilledSeries(String title) {
        XYSeries series = new TimeSeries(title);
        for (int i = 0; i < chartLength; i++)
            series.add(i, 0);
        return series;
    }

    private void setChartSettings() {
        renderer = new XYMultipleSeriesRenderer();

        XYSeriesRenderer xRenderer = new XYSeriesRenderer();
        xRenderer.setColor(Color.rgb(255, 100, 100));
        xRenderer.setLineWidth(5);

        XYSeriesRenderer yRenderer = new XYSeriesRenderer();
        yRenderer.setColor(Color.rgb(0, 255, 0));
        yRenderer.setLineWidth(5);

        XYSeriesRenderer zRenderer = new XYSeriesRenderer();
        zRenderer.setColor(Color.rgb(0, 255, 255));
        zRenderer.setLineWidth(5);

        renderer.setChartTitle(this.title);
        renderer.setChartTitleTextSize(40);
        renderer.addSeriesRenderer(xRenderer);
        renderer.addSeriesRenderer(yRenderer);
        renderer.addSeriesRenderer(zRenderer);
        renderer.setBackgroundColor(Color.BLACK);
        renderer.setMarginsColor(Color.BLACK);
        renderer.setYAxisMax(3);
        renderer.setYAxisMin(-3);
        renderer.setLabelsTextSize(20);
        renderer.setLegendTextSize(20);
        renderer.setPanEnabled(false);
        renderer.setInScroll(false);
    }

    public GraphicalView getChartView() {
        return chart;
    }

    public void update() {
        if (dataGetter == null) {
            return;
        }

        double newX = dataGetter.getX();
        double newY = dataGetter.getY();
        double newZ = dataGetter.getZ();
        updateSeries(seriesX, newX);
        updateSeries(seriesY, newY);
        updateSeries(seriesZ, newZ);
        chart.invalidate();
    }

    private void updateSeries(XYSeries series, double data) {

        series.remove(0);
        series.add(++count, data);
    }
}
