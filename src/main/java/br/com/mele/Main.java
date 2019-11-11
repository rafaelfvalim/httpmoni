package br.com.mele;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import java.awt.Toolkit;

public class Main {

	private JFrame frame;
	private double value = 1;
	private TimeSeries series;
	private HttpService service;
	private boolean exit = true;
	private boolean first = true;
	Logger logger = Logger.getLogger(Main.class.getName());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		logger.setLevel(Level.FINE);
		initLog();
		service = new HttpService();
		initialize();
		processJob();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/br/com/images/security_eye_30px.png")));
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBackground(Color.WHITE);
		frame.setBounds(100, 100, 791, 560);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Keeper Watcher");

		JPanel panelMoni = new JPanel();
		panelMoni.setBackground(Color.WHITE);
		panelMoni.setBounds(10, 11, 755, 458);
		frame.getContentPane().add(panelMoni);

		final XYDataset dataset = createDataset();
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(755, 458));
		chartPanel.setMouseZoomable(true, false);
		panelMoni.add(chartPanel);
		
		JLabel lblMonitorHttpGsw = new JLabel("Monitor HTTP GSW Keeper - By RValim");
		lblMonitorHttpGsw.setBounds(520, 480, 245, 14);
		frame.getContentPane().add(lblMonitorHttpGsw);

	}

	private XYDataset createDataset() {
		this.series = new TimeSeries("Random Data");
		Second current = new Second();
		double value = 1;
		try {
			this.series.add(current, new Double(value));
		} catch (SeriesException e) {
			System.err.println("Error adding to series");
		}
		return new TimeSeriesCollection(series);
	}

	private JFreeChart createChart(final XYDataset dataset) {
		return ChartFactory.createTimeSeriesChart("Monitor", "Tempo", "Resposta", dataset, false, false, false);
	}

	public void processJob() {
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Realizando consulta");
				if (!first) {

					try {
						value = 1;
						service.sendPost();
					} catch (Exception e1) {
//    				e1.printStackTrace();
						value = 0;
					}
					Second current = new Second();
					series.add(current, new Double(value));
				}
				first = false;
			}
		};
		javax.swing.Timer timer = new javax.swing.Timer(600000, action);
		timer.setInitialDelay(0);
		timer.setRepeats(true);
		timer.start();
	}

	public void initLog() {
		FileHandler fh;
		try {
			fh = new FileHandler("./log.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			logger.info("Inicio da aplicação");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
