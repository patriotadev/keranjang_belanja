import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class main{
    public Statement st;
    public ResultSet rs;
    public DefaultTableModel tabModel;
    Connection cn = koneksi.Koneksi();
   
    public main() {
       initialize();
    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
    public static void main(String args[]) {
       EventQueue.invokeLater(() -> {
         try {
             main window;
             window = new main();
         } catch (Exception e) {
         }
     }); 
    }
    
    public final void initialize()
    {
      
        JFrame mainFrame = new JFrame("RIO STORE");
        mainFrame.getContentPane().setBackground(Color.WHITE);
        mainFrame.setSize(600, 600);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
        
        JLabel namaProduk = new JLabel("Daftar Barang");
        mainFrame.add(namaProduk, BorderLayout.NORTH);
        
        JButton tambahBtn = new JButton("Tambah");
        mainFrame.add(tambahBtn, BorderLayout.AFTER_LINE_ENDS);
        
        JButton KerBtn = new JButton("Lihat Keranjang");
        mainFrame.add(KerBtn, BorderLayout.SOUTH);
        
        Object[] getData = {
            "Nama Barang","Harga"
        };
                 
        JTable jt=new JTable();     
        
        tabModel = new DefaultTableModel(null, getData);
        jt.setModel(tabModel);
        JScrollPane sp=new JScrollPane(jt);   
        mainFrame.add(sp, BorderLayout.CENTER);
        
//        ----

String state = "";
        
        try
        {
            st = cn.createStatement();
            tabModel.getDataVector().removeAllElements();
            tabModel.fireTableDataChanged();
            
            rs = st.executeQuery("SELECT * FROM barang " + state);
          
        while(rs.next())
        {
            Object[] data ={
                rs.getString("nama"),
                rs.getString("harga"),
            };
            
            tabModel.addRow(data);
        }
        
        } 
        catch(SQLException e)
        {
        }

//      ----
          
        KerBtn.addActionListener
        (
                new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {     
                            keranjangForm();                     
                        }
                    } 
        );
        
        tambahBtn.addActionListener
        (
                 new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {   
                            int column = 0;
                            int row = jt.getSelectedRow();
                            String namaP = jt.getModel().getValueAt(row, column).toString();
                            String hargaP = jt.getModel().getValueAt(row, 1).toString();
                            
                            System.out.println(namaP);
                            System.out.println(hargaP);
                            
                            
                            try{

                            st = cn.createStatement();
                            st.executeUpdate("INSERT INTO cart (nama, harga) VALUES ('" +namaP+ "','" +hargaP+"')");
                            System.out.println("Berhasil..");
         
                            JOptionPane.showMessageDialog(null, "Produk berhasil ditambahkan ke tabel!");

                            }
                            catch(SQLException a)
                            {}
        
                        }
                    }
        
        );
    }
    
    public void keranjangForm()
    {
        
        
         JFrame kerFrame = new JFrame("Keranjang Belanja");
         kerFrame.getContentPane().setBackground(Color.WHITE);
         kerFrame.setSize(600, 600);
         kerFrame.setResizable(false);
         kerFrame.setVisible(true);
         
         JButton hapusBtn = new JButton("Hapus");
         kerFrame.add(hapusBtn, BorderLayout.AFTER_LINE_ENDS);
 
        
        Object[] getData = {
            "Nama Barang","Harga"
        };
                 
        JTable jt=new JTable();     
        
        tabModel = new DefaultTableModel(null, getData);
        jt.setModel(tabModel);
        JScrollPane sp=new JScrollPane(jt);   
        kerFrame.add(sp, BorderLayout.CENTER);
        
//        ------

        String state = "";
        
        try
        {
            st = cn.createStatement();
            tabModel.getDataVector().removeAllElements();
            tabModel.fireTableDataChanged();
            
            rs = st.executeQuery("SELECT * FROM cart " + state);
          
        while(rs.next())
        {
            Object[] data ={
                rs.getString("nama"),
                rs.getString("harga"),
            };
            
            tabModel.addRow(data);
        }
        
        } 
        catch(SQLException e)
        {
        }


//      -----     

        hapusBtn.addActionListener
        (
                new ActionListener()
                {
                    @Override
                        public void actionPerformed(ActionEvent e)
                        {   
                            
                            int column = 0;
                            int row = jt.getSelectedRow();
                            String namaP = jt.getModel().getValueAt(row, column).toString();
                            String hargaP = jt.getModel().getValueAt(row, 1).toString();
                            System.out.println(namaP);
                            System.out.println(hargaP);
                            int th = Integer.parseInt(hargaP);
                            
                            
                            
                             try
                            {
                                int answer;
                                
                                if((answer = JOptionPane.showConfirmDialog(null, "Hapus barang?","Konfirmasi",JOptionPane.YES_NO_OPTION)) == 0)
                                {
                                    st = cn.createStatement();
                                    st.executeUpdate("DELETE FROM cart WHERE nama='"+namaP+"'");
                                    System.out.println("Sip..");
                                }
                            }
                            catch(SQLException a)
                            {
                                
                            }
                        }
                }
        
        );   
        
        
// Total
    try
    {
            st = cn.createStatement();
            
            rs = st.executeQuery("SELECT SUM(harga) AS total FROM cart ");
          
        while(rs.next())
        {
               String toha = rs.getString("total");
               
               JLabel th = new JLabel("Total Harga : " + toha);
               kerFrame.add(th, BorderLayout.SOUTH);
        }
        
        } 
        catch(SQLException e)
        {
            
        }
    
       
  
     }

    
    
}
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

