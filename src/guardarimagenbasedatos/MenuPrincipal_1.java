package guardarimagenbasedatos;

import java.awt.HeadlessException;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class MenuPrincipal_1 extends JDialog {
	private JLabel lblfoto;
	private JTextField txtcodigo;
	private JTextField txtnombre;
    FileInputStream fis;
    int longitudBytes;
    conexion con = Main.conexion;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MenuPrincipal_1 dialog = new MenuPrincipal_1();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MenuPrincipal_1() {
		setBounds(100, 100, 631, 480);
		getContentPane().setLayout(null);
		
		lblfoto = new JLabel("");
		lblfoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
		lblfoto.setBounds(437, 11, 168, 192);
		getContentPane().add(lblfoto);
		
		JButton btnImagen = new JButton("Imagen");
		btnImagen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AbrirImagen();
			}
		});
		btnImagen.setBounds(485, 214, 89, 23);
		getContentPane().add(btnImagen);
		
		txtcodigo = new JTextField();
		txtcodigo.setBounds(78, 50, 144, 20);
		getContentPane().add(txtcodigo);
		txtcodigo.setColumns(10);
		
		txtnombre = new JTextField();
		txtnombre.setBounds(78, 97, 304, 20);
		getContentPane().add(txtnombre);
		txtnombre.setColumns(10);
		
		JLabel lblCdigo = new JLabel("C\u00F3digo:");
		lblCdigo.setBounds(27, 53, 46, 14);
		getContentPane().add(lblCdigo);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(27, 100, 46, 14);
		getContentPane().add(lblNombre);
		
		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Guardar();
			}
		});
		btnGuardar.setBounds(73, 170, 89, 23);
		getContentPane().add(btnGuardar);
		
		JButton btnConsultar = new JButton("Consultar");
		btnConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Consultar();
			}
		});
		btnConsultar.setBounds(233, 170, 89, 23);
		getContentPane().add(btnConsultar);
		
		JButton btnDescarga = new JButton("Descarga");
		btnDescarga.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Descarga();
			}
		});
		btnDescarga.setBounds(200,300, 89, 23);
		getContentPane().add(btnDescarga);
		
		
	}
	private void AbrirImagen() {
		 lblfoto.setIcon(null);
	        JFileChooser j=new JFileChooser();
	        j.setFileSelectionMode(JFileChooser.FILES_ONLY);//solo archivos y no carpetas
	        int estado=j.showOpenDialog(null);
	        if(estado== JFileChooser.APPROVE_OPTION){
	            try{
	                fis=new FileInputStream(j.getSelectedFile());
	               // System.out.println(""+j.getSelectedFile());
	                //necesitamos saber la cantidad de bytes
	                this.longitudBytes=(int)j.getSelectedFile().length();
	                System.out.println(""+j.getSelectedFile().length());
	                try {
	                    Image icono=ImageIO.read(j.getSelectedFile()).getScaledInstance
	                            (lblfoto.getWidth(),lblfoto.getHeight(),Image.SCALE_DEFAULT);
	                    lblfoto.setIcon(new ImageIcon(icono));
	                    lblfoto.updateUI();

	                } catch (IOException ex) {
	                    JOptionPane.showMessageDialog(rootPane, "imagen: "+ex);
	                }
	            }catch(FileNotFoundException ex){
	                ex.printStackTrace();
	            }
	        }        // TODO add your handling code here:

	}
	private void Consultar() {
		con.crearConexion();
		String sql="select usuario_imagen from \"img\" where usuario_codigo = "+txtcodigo.getText()+";";
        ImageIcon foto;
        InputStream is;
        String nombre;
        try{
            ResultSet rs = con.ejecutarSQLSelect(sql);
            while(rs.next()){
                is = rs.getBinaryStream(1);
               // nombre = rs.getString(2);
                
                BufferedImage bi = ImageIO.read(is);
                foto = new ImageIcon(bi);
                
                Image img = foto.getImage();
                
               
                
                Image newimg = img.getScaledInstance(140, 170, java.awt.Image.SCALE_SMOOTH);
                
                ImageIcon newicon = new ImageIcon(newimg);
                
                lblfoto.setIcon(newicon);
                
                
                
              //  txtnombre.setText(nombre);
            }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(rootPane,"exception: "+ex);
            ex.printStackTrace();
           
        }

	}
	private void Guardar() {
		try{
            String sql="INSERT INTO \"img\"(usuario_codigo, usuario_nombre, usuario_imagen) VALUES (?, ?, ?)";

            PreparedStatement ps=con.getConexion().prepareStatement(sql);
            ps.setInt(1,Integer.parseInt(txtcodigo.getText()));
            ps.setString(2,txtnombre.getText());
            ps.setBinaryStream(3,fis,longitudBytes);
            ps.execute();
            ps.close();
           
            lblfoto.setIcon(null);
            txtcodigo.setText("");
            txtnombre.setText("");
            
            JOptionPane.showMessageDialog(rootPane,"Guardado correctamente");
        }catch(SQLException | NumberFormatException | HeadlessException x){
            JOptionPane.showMessageDialog(rootPane, "exception 2 "+x);
        }           // TODO add your handling code here:

	}
	
	private void Descarga(){
		
		//lblfoto
		
	}
}
