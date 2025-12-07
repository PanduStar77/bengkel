
package Controller;

import Model.Absen;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;


public class AbsenController {
    public Statement stm;
    public ResultSet res;
    public String sql;
    
    //model tabel/bentuk virtual sebelum di apply di view
    DefaultTableModel dtm = new DefaultTableModel();
    
    public AbsenController(){
       Koneksi db = new Koneksi();
       db.config();
       this.stm = db.stm;
       
    }
    public DefaultTableModel createTable(){
        this.dtm.setColumnCount(0);
        this.dtm.addColumn("id_absen");
        this.dtm.addColumn("id_Karyawan");
        this.dtm.addColumn("nama");
        this.dtm.addColumn("tanggal_absen");
        return this.dtm;
    }
    
    //method tambah absen/innsert
    public boolean tambahAbsen(String idKaryawan, String nama){
    //hubungkan dengan model
    Absen ab = new Absen();
    ab.setId_karyawan(idKaryawan);
    ab.setNama(nama);
    
    try{
        this.sql = "INSERT INTO tb_absen (id_karyawan, nama) VALUES ('"+ab.getId_karyawan()+"', '"+ab.getNama()+"')";
        //untuk insert, update,delete gunakan .executeUpdate(sql)

      
        this.stm.executeUpdate(sql);
        return true;
    }catch(Exception e){
        e.printStackTrace();
        return false;
    }
       
    }
    
    //method tampil riwayat:
    public void riwayatAbsen(String idKaryawan){
        try{
            this.dtm.getDataVector().removeAllElements();
            this.dtm.fireTableDataChanged();

            //querry riwayat absen
            this.sql = "SELECT id_absen, id_karyawan, nama, tanggal_absen FROM tb_absen WHERE id_karyawan = '"+idKaryawan+"' ORDER BY tanggal_absen DESC";

            //jalankan
            this.res = this.stm.executeQuery(sql);
            
            //masukan hasil querry ke dtm
            while(res.next()){
                Object[] obj = new Object[4];
                //nama harus
                obj[0] = res.getString("id_absen");
                obj[1] = res.getString("id_karyawan");
                obj[2] = res.getString("nama");
                obj[3] = res.getString("tanggal_absen");
                this.dtm.addRow(obj);
                
            }

            //masukan hasil querry
        }catch (Exception e) {
            System.out.println("Gagal query" + e);
        }
    }
    
}
