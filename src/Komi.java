import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.PaintEvent;


public class Komi {
	
	class Vector3i {
		int i;
		int x;
		int y;
		
		Vector3i(int i, int x, int y) {
			this.i = i;
			this.x = x;
			this.y = y;
		}
	}
	
	ArrayList<Vector3i> coordList = new ArrayList<Vector3i>();

	protected int ilMrowek;
	protected double odparowywanieFeromonu;
	protected double wspAlfa;
	protected double wspBeta;
	protected int iloscIteracjiMr;
	
	protected int ilKaraluchow;
	protected int dlugoscKroku;
	protected int zasiegWidocznosci;
	protected int iloscIteracji;
	
	protected int waga;
	
	protected boolean isMrowki;
	protected boolean isKaraluch;
	
	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	public static void main(String[] args) {
		try {
			Komi window = new Komi();
			window.open();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> mrowkowy(int ilMrowek, int ilIteracji, double alpha, double beta, double ro){
		TSP problem = new TSP(ilMrowek, alpha, beta, ro);
		long start = System.currentTimeMillis();
		ArrayList<String> result = problem.solve(ilIteracji);
		result.add("Time spent: " + (System.currentTimeMillis() - start) + " ms.");
		return result;
	}
	
	public ArrayList<String> karaluch(int ilKaraluchow, int zasiegWidocznosci, int dlugoscKroku, int iloscIteracji){
		File directory = new File (".");
        Road2 road = new Road2();
        ArrayList<String> result = null;
        Graph graph;
        try {
                graph = road.getWeightsFromFile(directory.getCanonicalPath() + "//out.txt");
                CockroachNest problem = new CockroachNest(graph, ilKaraluchow,zasiegWidocznosci,dlugoscKroku,iloscIteracji);
                long start = System.currentTimeMillis();
                result = problem.solve();
                result.add("Time spent: " + (System.currentTimeMillis() - start) + " ms.");
        } catch (IOException e) {
                e.printStackTrace();
        } 
        return result;
	}

	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	protected void createContents() {
		shell = new Shell();
        shell.setSize(854, 520);
        shell.setText("SWT Application");
        
        Label lblTytul = new Label(shell, SWT.NONE);
        lblTytul.setAlignment(SWT.CENTER);
        lblTytul.setFont(SWTResourceManager.getFont("Ubuntu", 18, SWT.NORMAL));
        lblTytul.setBounds(72, 10, 352, 32);
        lblTytul.setText("Wyznaczanie trasy");
        
        final Button btnAlgorytmMrwkowy = new Button(shell, SWT.CHECK);
        btnAlgorytmMrwkowy.setSelection(true);
        btnAlgorytmMrwkowy.setBounds(10, 48, 202, 24);
        btnAlgorytmMrwkowy.setText("algorytm mrowkowy");
        
        final Button btnAlgorytmKaralucha = new Button(shell, SWT.CHECK);
        btnAlgorytmKaralucha.setBounds(10, 260, 166, 24);
        btnAlgorytmKaralucha.setText("algorytm karalucha");
        
        Label lblIloscMrowek = new Label(shell, SWT.NONE);
        lblIloscMrowek.setBounds(10, 78, 121, 24);
        lblIloscMrowek.setText("Ilosc mrowek");
        
        final Spinner spinIloscMrowek = new Spinner(shell, SWT.BORDER);
        spinIloscMrowek.setIncrement(1);
        spinIloscMrowek.setPageIncrement(100);
        spinIloscMrowek.setMaximum(1000000);
        spinIloscMrowek.setMinimum(1);
        spinIloscMrowek.setSelection(200);
        spinIloscMrowek.setBounds(137, 78, 87, 24);
        
        Label lblIloscIteracjiMr = new Label(shell, SWT.NONE);
        lblIloscIteracjiMr.setText("Ilosc iteracji");
        lblIloscIteracjiMr.setBounds(10, 108, 121, 24);
        
        final Spinner spinIloscIteracjiMr = new Spinner(shell, SWT.BORDER);
        spinIloscIteracjiMr.setIncrement(1);
        spinIloscIteracjiMr.setPageIncrement(100);
        spinIloscIteracjiMr.setMaximum(1000000);
        spinIloscIteracjiMr.setMinimum(1);
        spinIloscIteracjiMr.setSelection(100);
        spinIloscIteracjiMr.setBounds(137, 108, 87, 24);        
        
        Label lblWspczynnikAlfa = new Label(shell, SWT.NONE);
        lblWspczynnikAlfa.setBounds(10, 138, 121, 24);
        lblWspczynnikAlfa.setText("Wspolczynnik alfa");
        
        final Spinner spinWspolczynnikAlfa = new Spinner(shell, SWT.BORDER);
        spinWspolczynnikAlfa.setIncrement(1);
        spinWspolczynnikAlfa.setMinimum(1);
        spinWspolczynnikAlfa.setSelection(2);
        spinWspolczynnikAlfa.setMaximum(200);
        spinWspolczynnikAlfa.setBounds(137, 138, 87, 24);
        
        Label lblWspczynnikBeta = new Label(shell, SWT.NONE);
        lblWspczynnikBeta.setBounds(10, 168, 121, 24);
        lblWspczynnikBeta.setText("Wspolczynnik beta");
        
        final Spinner spinWspolczynnikBeta = new Spinner(shell, SWT.BORDER);
        spinWspolczynnikBeta.setIncrement(1);
        spinWspolczynnikBeta.setMinimum(1);
        spinWspolczynnikBeta.setSelection(96);
        spinWspolczynnikBeta.setMaximum(200);
        spinWspolczynnikBeta.setBounds(137, 168, 87, 24);
        
        Label lblIntensywnoscFeromonu = new Label(shell, SWT.NONE);
        lblIntensywnoscFeromonu.setBounds(10, 198, 121, 40);
        lblIntensywnoscFeromonu.setText("Odparowywanie\n feromonu");
        
        final Spinner spinIntensywnoscFeromonu = new Spinner(shell, SWT.BORDER);
        spinIntensywnoscFeromonu.setIncrement(1);
        spinIntensywnoscFeromonu.setMinimum(0);
        spinIntensywnoscFeromonu.setSelection(1);
        spinIntensywnoscFeromonu.setMaximum(10);
        spinIntensywnoscFeromonu.setBounds(137, 198, 87, 24);
        
        
        Label lblMinimalizuj = new Label(shell, SWT.NONE);
        lblMinimalizuj.setFont(SWTResourceManager.getFont("Ubuntu", 12, SWT.NORMAL));
        lblMinimalizuj.setBounds(10, 437, 100, 32);
        lblMinimalizuj.setText("Minimalizuj:");
        
        Label lblIloscKaraluchow = new Label(shell, SWT.NONE);
        lblIloscKaraluchow.setBounds(10, 290, 119, 27);
        lblIloscKaraluchow.setText("Ilosc karaluchow");
        
        final Spinner spinIloscKaraluchow = new Spinner(shell, SWT.BORDER);
        spinIloscKaraluchow.setIncrement(10);
        spinIloscKaraluchow.setMaximum(1000000);
        spinIloscKaraluchow.setMinimum(1);
        spinIloscKaraluchow.setSelection(100);
        spinIloscKaraluchow.setBounds(137, 323, 87, 27);
        
        Label lblDlugoscKroku = new Label(shell, SWT.NONE);
        lblDlugoscKroku.setBounds(10, 389, 119, 27);
        lblDlugoscKroku.setText("Dlugosc kroku");
        
        final Spinner spinDlugoscKroku = new Spinner(shell, SWT.BORDER);
        spinDlugoscKroku.setIncrement(1);
        spinDlugoscKroku.setMaximum(1000);
        spinDlugoscKroku.setMinimum(1);
        spinDlugoscKroku.setSelection(2);
        spinDlugoscKroku.setBounds(137, 389, 87, 27);
        
        Label lblZasiegWidocznosci = new Label(shell, SWT.NONE);
        lblZasiegWidocznosci.setBounds(10, 356, 119, 24);
        lblZasiegWidocznosci.setText("Zasieg widocznosci");
        
        final Spinner spinZasiegWidocznosci = new Spinner(shell, SWT.BORDER);
        spinZasiegWidocznosci.setIncrement(1);
        spinZasiegWidocznosci.setMaximum(1000);
        spinZasiegWidocznosci.setMinimum(1);
        spinZasiegWidocznosci.setSelection(3);
        spinZasiegWidocznosci.setBounds(137, 356, 87, 27);
        
        Label lblIloscIteracji = new Label(shell, SWT.NONE);
        lblIloscIteracji.setBounds(10, 323, 119, 27);
        lblIloscIteracji.setText("Ilosc iteracji");
        
        final Spinner spinIloscIteracji = new Spinner(shell, SWT.BORDER);
        spinIloscIteracji.setIncrement(1);
        spinIloscIteracji.setPageIncrement(100);
        spinIloscIteracji.setMaximum(1000000);
        spinIloscIteracji.setMinimum(1);
        spinIloscIteracji.setSelection(100);
        spinIloscIteracji.setBounds(137, 290, 87, 27);
		
		Button btnOblicz = new Button(shell, SWT.NONE);
		btnOblicz.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				isMrowki = btnAlgorytmMrwkowy.getSelection();
				isKaraluch = btnAlgorytmKaralucha.getSelection();
				
				ilMrowek = spinIloscMrowek.getSelection();
				iloscIteracjiMr = spinIloscIteracjiMr.getSelection();
				odparowywanieFeromonu = spinIntensywnoscFeromonu.getSelection() / 10.0;
				wspAlfa = spinWspolczynnikAlfa.getSelection() / 10.0;
				wspBeta = spinWspolczynnikBeta.getSelection() / 10.0;
				
				ilKaraluchow = spinIloscKaraluchow.getSelection();
				dlugoscKroku = spinDlugoscKroku.getSelection();
				zasiegWidocznosci = spinZasiegWidocznosci.getSelection();
				iloscIteracji = spinIloscIteracji.getSelection();
				
			//	wczytajDane();
				ArrayList<String> mrowkowyResult = mrowkowy(ilMrowek, iloscIteracjiMr, wspAlfa, wspBeta, odparowywanieFeromonu);
				ArrayList<String> karaluchResult = karaluch(ilKaraluchow, zasiegWidocznosci, dlugoscKroku, iloscIteracji);
				
				String mrowkowyFinal = "", karaluchFinal = "";
				
				for(String s: mrowkowyResult) {
					mrowkowyFinal = mrowkowyFinal.concat(s + "\n");
				}
				for(String s: karaluchResult) {
					karaluchFinal = karaluchFinal.concat(s + "\n");
				}
				
				ResultShell resultShell = new ResultShell(Display.getCurrent());
				resultShell.setResults(mrowkowyFinal, karaluchFinal);
				resultShell.open();
			}
		});
		btnOblicz.setBounds(530, 437, 87, 40);
		btnOblicz.setText("Oblicz");
		
		final List list = new List(shell, SWT.BORDER | SWT.V_SCROLL);
		list.setBounds(647, 49, 166, 368);
		
		final Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				boolean exists = false;
				
				for(Vector3i coord: coordList) {
					if(coord.x == e.x && coord.y == e.y)
						exists = true;
				}
				
				if(!exists) {
					list.add(coordList.size() + " " + e.x + " " + e.y);
					coordList.add(new Vector3i(coordList.size(), e.x, e.y));
					canvas.redraw();
				}
			}
		});
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				for(Vector3i coord: coordList) {
					e.gc.drawRectangle(coord.x, coord.y, 2, 2);
				}
			}
		});
		canvas.setBounds(249, 48, 368, 368);
		
		Button btnSave = formToolkit.createButton(shell, "Zapisz", SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter("out.txt"));
					for(Vector3i coord: coordList) {
						bw.write("" + coord.i + ' ' + coord.x + ' ' + coord.y);
						bw.newLine();
					}
					bw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSave.setBounds(726, 437, 87, 40);
		
		Button btnLoad = new Button(shell, SWT.NONE);
		btnLoad.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(shell, SWT.OPEN);
		        fd.setText("Wczytaj");
		        fd.setFilterPath("C:/");
		        String[] filterExt = { "*.txt", "*.*" };
		        fd.setFilterExtensions(filterExt);
		        String selected = fd.open();
		        
		        BufferedReader br;
		        
		        try {
		        	br = new BufferedReader(new FileReader(selected));
		        	
					while(br.ready()) {
					    String line = br.readLine();
					    
					    list.add(line);
					    String[] separated = line.split(" ");
					    
						coordList.add(new Vector3i(Integer.parseInt(separated[0]), Integer.parseInt(separated[1]), Integer.parseInt(separated[2])));
					}
					br.close();
					
					canvas.redraw();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        
			}
		});
		btnLoad.setBounds(633, 437, 87, 40);
		formToolkit.adapt(btnLoad, true, true);
		btnLoad.setText("Wczytaj");

	}
}