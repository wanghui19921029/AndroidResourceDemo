package com.wh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class GenerateValueFiles {

	private int baseW;
	private int baseH;

	private String dirStr = "./res";

	private final static String WTemplate = "<dimen name=\"layout_size_{0}_dip\">{1}px</dimen>\n";
	private final static String HTemplate = "<dimen name=\"layout_size_{0}_dip\">{1}px</dimen>\n";

	/**
	 * {0}-HEIGHT
	 */
	private final static String VALUE_TEMPLATE = "values-{0}x{1}";

	private static final String SUPPORT_DIMESION = "320,480;480,800;480,854;540,960;600,1024;720,1184;720,1196;720,1280;768,1024;768,1280;800,1280;1080,1812;1080,1920;1440,2560;";

	private String supportStr = SUPPORT_DIMESION;

	public GenerateValueFiles(int baseX, int baseY) {
		this.baseW = baseX;
		this.baseH = baseY;

		if (!this.supportStr.contains(baseX + "," + baseY)) {
			this.supportStr += baseX + "," + baseY + ";";
		}

		File dir = new File(dirStr);
		if (!dir.exists()) {
			dir.mkdir();

		}
		System.out.println(dir.getAbsoluteFile());
	}

	public void generate() {
		String[] vals = supportStr.split(";");
		for (String val : vals) {
			String[] wh = val.split(",");
			generateXmlFile(Integer.parseInt(wh[0]), Integer.parseInt(wh[1]));
		}
	}

	private void generateXmlFile(int w, int h) {
		StringBuffer sbForWidth = new StringBuffer();
		sbForWidth.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		sbForWidth.append("<resources>\n");
		float cellw = w * 1.0f / baseW;

		System.out.println("width : " + w + "," + baseW + "," + cellw);
		for (int i = 1; i < baseW; i++) {
			sbForWidth.append(WTemplate.replace("{0}", i + "").replace("{1}",
					change(cellw * i) + ""));
		}
		sbForWidth.append(WTemplate.replace("{0}", baseW + "").replace("{1}",
				w + ""));
		sbForWidth.append("</resources>");

		StringBuffer sbForHeight = new StringBuffer();
		sbForHeight.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		sbForHeight.append("<resources>\n");
		float cellh = h * 1.0f / baseH;
		System.out.println("height : " + h + "," + baseH + "," + cellh);
		for (int i = 1; i < baseH; i++) {
			sbForHeight.append(HTemplate.replace("{0}", i + "").replace("{1}",
					change(cellh * i) + ""));
		}
		sbForHeight.append(HTemplate.replace("{0}", baseH + "").replace("{1}",
				h + ""));
		sbForHeight.append("</resources>");

		File fileDir = new File(dirStr + File.separator
				+ VALUE_TEMPLATE.replace("{0}", h + "").replace("{1}", w + ""));
		fileDir.mkdir();

		File layxFile = new File(fileDir.getAbsolutePath(), "lay_x.xml");
		File layyFile = new File(fileDir.getAbsolutePath(), "lay_y.xml");

		System.out.println("path = " + fileDir.getAbsolutePath());
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(layxFile));
			pw.print(sbForWidth.toString());
			pw.close();
			pw = new PrintWriter(new FileOutputStream(layyFile));
			pw.print(sbForHeight.toString());
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static float change(float a) {
		int temp = (int) (a * 100);
		return temp / 100f;
	}

	public static void main(String[] args) {
		// 以此为标准，其他的分辨率可以计算出来
		int baseW = 720;
		int baseH = 1280;
		new GenerateValueFiles(baseW, baseH).generate();
	}
}
