package com.xuanzhi.tools.monitor;

import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import java.awt.Color;

public class GenerateChart {

	public static BufferedImage generate(MonitorService service,LinkedList<MonitorPoint> points){
		MonitorPoint ps[] = points.toArray(new MonitorPoint[0]);
		TimeSeriesCollection dataset = new TimeSeriesCollection ();
		
		Map<String,String> seriesMap = service.getChartSeriesPropertyName();
		
		if(seriesMap == null) return null;
		
		Iterator<String> it = seriesMap.keySet().iterator();
		while(it.hasNext()){
			String property = it.next();
			String seriesName = seriesMap.get(property);
			
			TimeSeries series = new TimeSeries(seriesName,Minute.class);
			
			double defaultValue = 0;
			for(int i = ps.length-1; i >= 0 ; i--){
				defaultValue = ps[i].data.getDataAsDouble(property,defaultValue);
				series.addOrUpdate(new Minute(new Date(ps[i].checkTime)),defaultValue);
			}
			
			dataset.addSeries(series);
		}
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart(service.getName(),null,service.getChartRangName(),
				dataset,
				true,
                true,
                false);
		chart.setBackgroundPaint(new Color(240,240,240));
		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(new Color(216,216,216));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		//plot.setAxisOffset(new java.awt.RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		
		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(false);
			renderer.setBaseShapesFilled(false);
		}
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
		
		return chart.createBufferedImage(1024,250);
	}
}
