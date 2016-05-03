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

import java.util.List;

import mis.kuas.data.AccDataGetter;

public class AccChart {

    private XYSeries seriesX;
    private XYSeries seriesY;
    private XYSeries seriesZ;
    private XYMultipleSeriesDataset dataset;
    private XYMultipleSeriesRenderer renderer;
    private GraphicalView chart;
    private String title;

    public AccChart(Context context, String title) {
        this.title = title;
        buildDataset();
        setChartSettings();
        this.chart = ChartFactory.getLineChartView(context, dataset, renderer);
        this.chart.setBackgroundColor(Color.BLACK);
    }

    private void buildDataset() {
        seriesX = new TimeSeries("X");
        seriesY = new TimeSeries("Y");
        seriesZ = new TimeSeries("Z");
        dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(seriesX);
        dataset.addSeries(seriesY);
        dataset.addSeries(seriesZ);
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
//        renderer.setPanEnabled(false);
//        renderer.setInScroll(false);
    }

    public GraphicalView getChartView() {
        return chart;
    }

    public void setDataList(List<AccDataGetter> dataList) {
        seriesX.clear();
        seriesY.clear();
        seriesZ.clear();
        int count = 0;
        for (AccDataGetter dataGetter : dataList) {
            seriesX.add(count, dataGetter.getX());
            seriesY.add(count, dataGetter.getY());
            seriesZ.add(count, dataGetter.getZ());
            count++;
        }
        chart.invalidate();
    }

}
