package Controller;

import Model.Pegawai;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

public class PegawaiController {

    public Statement stm;
    public ResultSet res;
    public String sql;

    DefaultTableModel dtm = new DefaultTableModel();

    //konstruktor
    public PegawaiController() {
        //objek koneksi
        Koneksi db = new Koneksi();
        db.config();
        this.stm = db.stm;
    }

    //method cekLogin(select)
    public boolean cekLogin(String un, String pw) {
        //dipetakan dengan model
        Pegawai pgw = new Pegawai();
        pgw.setNama(un);
        pgw.setPassword(pw);
        boolean status = false;

        //query ke database + cek
        try {
            //sql query
            this.sql = "SELECT * FROM tb_pegawai WHERE nama = '" + pgw.getNama() + "'AND password = '" + pgw.getPassword() + "'";

            //menjalankan query
            //khusus select gunakan 'executeQuery'
            this.res = this.stm.executeQuery(sql);

            //pengecekan
            if (res.next()) {
                status = true;
            } else {
                status = false;
            }
        } catch (Exception e) {
            System.out.println("Query Gagal");
        }
        return status;
    }

    public DefaultTableModel createTableGaji() {
        dtm.addColumn("ID Karyawan");
        dtm.addColumn("Nama");
        dtm.addColumn("Gaji ");
        dtm.addColumn("Status");
        return this.dtm;
    }

    /*
    jgjg nak butuh uey, nak pengen nampilin data pegawai
    public DefaultTableModel createTablePegawai() {
        this.dtm = new DefaultTableModel(); // Reset dtm
        dtm.addColumn("ID Pegawai");
        dtm.addColumn("Nama");
        dtm.addColumn("No. Telp");
        dtm.addColumn("Email");
        dtm.addColumn("Status");
        return this.dtm;
    }
     */
    //public tampilkan Gaji
    public void tampilkanGaji() {
        try {
            // Persiapkan table virtual
            this.dtm.getDataVector().removeAllElements();
            this.dtm.fireTableDataChanged();
            //hubungin 2 tb
            this.sql = "SELECT p.id_pegawai, p.nama, g.gaji, g.status_pengambilan_gaji"
                            + " FROM tb_pegawai p "
                            + "LEFT JOIN tb_gaji g ON p.id_pegawai = g.id_karyawan "
                            + "ORDER BY p.id_pegawai ASC";

            this.res = this.stm.executeQuery(sql);

            while (res.next()) {
                Object[] obj = new Object[4];

                obj[0] = res.getString("id_pegawai");
                obj[1] = res.getString("nama");
                obj[2] = res.getString("gaji");
                obj[3] = res.getString("status_pengambilan_gaji");

                this.dtm.addRow(obj);
            }
        } catch (Exception e) {
            System.out.println("Gagal query: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //method  ambil gaji: 
    //klarifikasi dulu cui, gak kusambungin ke model, karena setelah klik tombol ambil gaji, gajinya langsung 0 bukan sesuai inputan user.

    public boolean ambilGaji(String idPegawai) {
        try {
            this.sql = "UPDATE tb_gaji SET status_pengambilan_gaji = 'SUDAH DIAMBIL', gaji = 0 WHERE id_karyawan = '" + idPegawai + "' AND status_pengambilan_gaji = 'BELUM DIAMBIL'";
            this.stm.executeUpdate(sql);
            // int rowsAffected = this.stm.executeUpdate(sql);
            //  return rowsAffected >0;
            return true;
        } catch (Exception e) {
            System.out.println("guerry gagal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //method  update 
    public boolean ubahData(String idPegawai, String noBaru, String emailBaru) {
        //hubungkan ke model
        Pegawai pgw = new Pegawai();
        pgw.setId_pegawai(idPegawai);
        pgw.setNo_telp(noBaru);
        pgw.setEmail(emailBaru);

        try {
            this.sql = "UPDATE tb_pegawai SET no_telp ='" + pgw.getNo_telp() + "', email =  '" + pgw.getEmail() + "' WHERE id_pegawai = '" + pgw.getId_pegawai() + "'";
            this.stm.executeUpdate(sql);
            // int rowsAffected = this.stm.executeUpdate(sql);
            //  return rowsAffected >0;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //method tampilkan full pegawai
    /*public void tampilkanPegawai() {
        try {
            this.dtm.getDataVector().removeAllElements();
            this.dtm.fireTableDataChanged();
            
            this.sql = "SELECT id_pegawai, nama, no_telp, email, status FROM tb_pegawai ORDER BY id_pegawai ASC";

            this.res = this.stm.executeQuery(sql);

            while (res.next()) {
                Object[] obj = new Object[5];
                obj[0] = res.getString("id_pegawai");
                obj[1] = res.getString("nama");
                obj[2] = res.getString("no_telp");
                obj[3] = res.getString("email");
                obj[4] = res.getString("status");

                this.dtm.addRow(obj);
            }
        } catch (Exception e) {
            System.out.println("Gagal query tampilkan Pegawai: " + e.getMessage());
            e.printStackTrace();
        }
    }*/
}
