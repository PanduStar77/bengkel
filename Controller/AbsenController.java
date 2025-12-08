package Controller;

import Model.Absen;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

public class AbsenController {

    public Statement stm;
    public ResultSet res;
    public String sql;

    

    public AbsenController() {
        Koneksi db = new Koneksi();
        db.config();
        this.stm = db.stm;

    }


    //method tambah absen/innsert
    public boolean tambahAbsen(String idKaryawan, String nama) {
        //hubungkan dengan model
        Absen ab = new Absen();
        ab.setId_karyawan(idKaryawan);
        ab.setNama(nama);

        try {
            this.sql = "INSERT INTO tb_absen (id_karyawan, nama) VALUES ('" + ab.getId_karyawan() + "', '" + ab.getNama() + "')";
            //untuk insert, update,delete gunakan .executeUpdate(sql)

            this.stm.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    //method tampil riwayat pribadi
    public DefaultTableModel riwayatAbsen(String idKaryawan) {
        DefaultTableModel dtmrap = new  DefaultTableModel();
        
        dtmrap.addColumn("ID Absen");
        dtmrap.addColumn("ID Karyawan");
        dtmrap.addColumn("Nama");
        dtmrap.addColumn("Tanggal Absen");
        
        try{
            this.sql = "SELECT id_absen, id_karyawan, nama, tanggal_absen FROM tb_absen WHERE id_karyawan = '" + idKaryawan + "' ORDER BY tanggal_absen DESC";

            this.res = this.stm.executeQuery(sql);
            
            while (res.next()) {
                Object[] obj = new Object[4];
                obj[0] = res.getString("id_absen");
                obj[1] = res.getString("id_karyawan");
                obj[2] = res.getString("nama");
                obj[3] = res.getString("tanggal_absen");
                dtmrap.addRow(obj);
            }
            return dtmrap;
        }catch (Exception e) {
            System.out.println("query gagal : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public DefaultTableModel  riwayatAbsenSemua() {
        DefaultTableModel dtmSemua = new DefaultTableModel();
        
        dtmSemua.addColumn("ID Absen");
        dtmSemua.addColumn("ID Karyawan");
        dtmSemua.addColumn("Nama");
        dtmSemua.addColumn("Tanggal Absen");
        
        try {
            // Menampilkan semua data
            this.sql = "SELECT id_absen, id_karyawan, nama, tanggal_absen FROM tb_absen ORDER BY tanggal_absen DESC";

            this.res = this.stm.executeQuery(sql);

            while (res.next()) {
                Object[] obj = new Object[4];
                obj[0] = res.getString("id_absen");
                obj[1] = res.getString("id_karyawan");
                obj[2] = res.getString("nama");
                obj[3] = res.getString("tanggal_absen");
                dtmSemua.addRow(obj);
            }
            return dtmSemua;
        } catch (Exception e) {
            System.out.println("querry gagal " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
