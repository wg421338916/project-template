package com.wanggang.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.MiniTableRenderData;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.util.BytePictureUtils;
import com.google.common.collect.Maps;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Map;

/**
 * 转模板测试 http://deepoove.com/poi-tl/
 * 图表：https://blog.csdn.net/stayingalone/article/details/53319366
 *
 * @author wg
 * @version 1.0
 * @date 2020/5/21 16:46
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.8
 */
public class PoiTlAndFreeChartStudyTest {

    /**
     * 参考：http://deepoove.com/poi-tl/
     *
     * @throws IOException
     */
    @Test
    public void test() throws IOException {
        String baseDir = System.getProperty("user.dir") + "\\src\\test\\java\\files\\";

        RowRenderData header = RowRenderData.build(new TextRenderData("FF00FF", "姓名"), new TextRenderData("FF00FF", "学历"));
        RowRenderData row0 = RowRenderData.build("张三", "研究生");
        RowRenderData row1 = RowRenderData.build("李四", "博士");
        RowRenderData row2 = RowRenderData.build("王五", "小学生");


        DefaultPieDataset dpd = new DefaultPieDataset(); //建立一个默认的饼图
        dpd.setValue("管理人员", 25);  //输入数据
        dpd.setValue("市场人员", 25);
        dpd.setValue("开发人员", 45);
        dpd.setValue("其他人员", 10);

        Map<String, Object> map = Maps.newHashMap();
        //文本测试
        map.put("title", "poi-tl Word模板引擎");
        //图片测试
        map.put("img1", new PictureRenderData(500, 230, ".png", BytePictureUtils.getLocalBufferedImage(createLineChart("test", "x", "y", createDataset()))));

        double[] data1 = new double[]{1, 2, 3};
        String[] data2 = new String[]{"1", "2", "3"};
        String[] keys = new String[]{"key1", "key2", "key3"};
        map.put("img2", new PictureRenderData(500, 230, ".png", BytePictureUtils.getLocalBufferedImage(createPieChart3D("chartTitle", getDataPieSetByUtil(data1, data2), keys))));

        //表格测试
        map.put("table", new MiniTableRenderData(header, Arrays.asList(row0, row1, row2)));

        try (XWPFTemplate template = XWPFTemplate.compile(baseDir + "template.docx")) {
            template.render(map);

            try (FileOutputStream out = new FileOutputStream(baseDir + "output.docx")) {
                template.write(out);
                out.flush();
            }
        }

        Assert.assertTrue(FileUtil.exist(baseDir + "output.docx"));
    }

    /**
     * 折线图
     *
     * @param chartTitle
     * @param x
     * @param y
     * @param dataset
     * @return
     */
    public static File createLineChart(String chartTitle, String x, String y, CategoryDataset dataset) throws IOException {

        // 构建一个chart
        JFreeChart chart = ChartFactory.createLineChart(
                chartTitle,
                x,
                y,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        //字体清晰
        chart.setTextAntiAlias(false);
        // 设置背景颜色
        chart.setBackgroundPaint(Color.WHITE);

        // 设置图标题的字体
        Font font = new Font("隶书", Font.BOLD, 25);
        chart.getTitle().setFont(font);

        // 设置面板字体
        Font labelFont = new Font("SansSerif", Font.TRUETYPE_FONT, 12);
        // 设置图示的字体
        chart.getLegend().setItemFont(labelFont);

        CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
        // x轴 // 分类轴网格是否可见
        categoryplot.setDomainGridlinesVisible(true);
        // y轴 //数据轴网格是否可见
        categoryplot.setRangeGridlinesVisible(true);
        categoryplot.setRangeGridlinePaint(Color.WHITE);// 虚线色彩
        categoryplot.setDomainGridlinePaint(Color.WHITE);// 虚线色彩
        categoryplot.setBackgroundPaint(Color.lightGray);// 折线图的背景颜色

        // 设置轴和面板之间的距离
        // categoryplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));

        // 横轴 x
        CategoryAxis domainAxis = categoryplot.getDomainAxis();
        domainAxis.setLabelFont(labelFont);// 轴标题
        domainAxis.setTickLabelFont(labelFont);// 轴数值
        // domainAxis.setLabelPaint(Color.BLUE);//轴标题的颜色
        // domainAxis.setTickLabelPaint(Color.BLUE);//轴数值的颜色

        // 横轴 lable 的位置 横轴上的 Lable 45度倾斜 DOWN_45
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);

        // 设置距离图片左端距离
        domainAxis.setLowerMargin(0.0);
        // 设置距离图片右端距离
        domainAxis.setUpperMargin(0.0);

        // 纵轴 y
        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
        numberaxis.setLabelFont(labelFont);
        numberaxis.setTickLabelFont(labelFont);
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberaxis.setAutoRangeIncludesZero(true);

        // 获得renderer 注意这里是下嗍造型到lineandshaperenderer！！
        LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot
                .getRenderer();
        lineandshaperenderer.setBaseShapesVisible(true); // series 点（即数据点）可见
        lineandshaperenderer.setBaseLinesVisible(true); // series 点（即数据点）间有连线可见

        // 显示折点数据
        lineandshaperenderer
                .setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        lineandshaperenderer.setBaseItemLabelsVisible(true);

        return generatePng(chart);
    }

    /**
     * 柱状图数据集
     *
     * @return
     */
    public static CategoryDataset createDataset() {
        String str1 = "Java EE开发";
        String str2 = "IOS开发";
        String str3 = "Android开发";
        String str4 = "1月";
        String str5 = "2月";
        String str6 = "3月";
        String str7 = "4月";
        String str8 = "5月";

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1.0D, str1, str4);
        dataset.addValue(4.0D, str1, str5);
        dataset.addValue(3.0D, str1, str6);
        dataset.addValue(5.0D, str1, str7);
        dataset.addValue(5.0D, str1, str8);

        dataset.addValue(5.0D, str2, str4);
        dataset.addValue(7.0D, str2, str5);
        dataset.addValue(6.0D, str2, str6);
        dataset.addValue(8.0D, str2, str7);
        dataset.addValue(4.0D, str2, str8);

        dataset.addValue(4.0D, str3, str4);
        dataset.addValue(3.0D, str3, str5);
        dataset.addValue(2.0D, str3, str6);
        dataset.addValue(3.0D, str3, str7);
        dataset.addValue(6.0D, str3, str8);
        return dataset;
    }

    /**
     * 生成饼图
     *
     * @param chartTitle 图的标题
     * @param dataset    数据集
     * @param pieKeys    分饼的名字集
     * @return
     */
    public static File createPieChart3D(String chartTitle, PieDataset dataset, String[] pieKeys) throws IOException {
        JFreeChart chart = ChartFactory.createPieChart3D(
                chartTitle,
                dataset,
                true,//显示图例
                true,
                false);

        //关闭抗锯齿，是字体清晰
        chart.getRenderingHints().put(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        chart.setTextAntiAlias(false);
        //图片背景色
        chart.setBackgroundPaint(Color.white);
        //设置图标题的字体重新设置title
        Font font = new Font("隶书", Font.BOLD, 25);
        chart.getTitle().setFont(font);
    /*TextTitle title = new TextTitle(chartTitle);
    title.setFont(font);
    chart.setTitle(title);*/
        //设置图例字体
        chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 14));

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        // 图片中显示百分比:默认方式

        // 指定饼图轮廓线的颜色
        // plot.setBaseSectionOutlinePaint(Color.BLACK);
        // plot.setBaseSectionPaint(Color.BLACK);

        // 设置无数据时的信息
        plot.setNoDataMessage("无对应的数据，请重新查询。");

        // 设置无数据时的信息显示颜色
        plot.setNoDataMessagePaint(Color.red);

        // 图片中显示百分比:自定义方式，{0} 表示选项，
        //{1} 表示数值， {2} 表示所占比例 ,小数点后两位
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}={1}({2})", NumberFormat.getNumberInstance(),
                new DecimalFormat("0.00%")));
        //图片显示字体
        plot.setLabelFont(new Font("宋体", Font.TRUETYPE_FONT, 12));

        // 图例显示百分比:自定义方式， {0} 表示选项，
        //{1} 表示数值， {2} 表示所占比例
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}={1}({2})"));

        // 指定图片的透明度(0.0-1.0)
        plot.setForegroundAlpha(0.65f);
        // 指定显示的饼图上圆形(false)还椭圆形(true)
        plot.setCircular(false, true);

        // 设置第一个 饼块section 的开始位置，默认是12点钟方向
        plot.setStartAngle(90);

        // // 设置分饼颜色
        plot.setSectionPaint(pieKeys[0], new Color(244, 194, 144));
        plot.setSectionPaint(pieKeys[1], new Color(144, 233, 144));

        return generatePng(chart);
    }

    // 饼状图 数据集
    public static PieDataset getDataPieSetByUtil(double[] data, String[] datadescription) {

        if (data != null && datadescription != null) {
            if (data.length == datadescription.length) {
                DefaultPieDataset dataset = new DefaultPieDataset();
                for (int i = 0; i < data.length; i++) {
                    dataset.setValue(datadescription[i], data[i]);
                }
                return dataset;
            }
        }
        return null;
    }

    public static File generatePng(JFreeChart chart) throws IOException {
        String baseUrl = "D:\\data\\bjtemplate\\chart\\";
        File file = new File(baseUrl);
        if (!file.exists()) {
            file.mkdirs();
        }

        String chartName = baseUrl + UUID.fastUUID().toString();


        try (FileOutputStream fosChart = new FileOutputStream(chartName);) {
            //文件夹不存在则创建

            //高宽的设置影响椭圆饼图的形状
            ChartUtilities.writeChartAsPNG(fosChart, chart, 500, 230);
            return FileUtil.file(chartName);
        }
    }
}
