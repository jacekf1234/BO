import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class ResultShell extends Shell {
	private Text text;
	private Text text_1;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			ResultShell shell = new ResultShell(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public ResultShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		Label lblMrwkowy = new Label(this, SWT.NONE);
		lblMrwkowy.setBounds(125, 25, 59, 15);
		lblMrwkowy.setText("Mr\u00F3wkowy");
		
		Label lblKaraluch = new Label(this, SWT.NONE);
		lblKaraluch.setBounds(448, 25, 55, 15);
		lblKaraluch.setText("Karaluch");
		
		text = new Text(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		text.setEditable(false);
		text.setBounds(10, 46, 300, 438);
		
		text_1 = new Text(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		text_1.setEditable(false);
		text_1.setBounds(324, 46, 300, 438);
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Wyniki");
		setSize(650, 533);
	}
	
	public void setResults(String mrowkowy, String karaluch) {

		text.setText(mrowkowy);

		text_1.setText(karaluch);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
