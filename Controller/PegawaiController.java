package Controller;

import Model.Pegawai;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

public class PegawaiController {

    public Statement stm;
    public ResultSet res;
    public String sql;

    //konstruktor
    public PegawaiController() {
        //objek koneksi
        Koneksi db = new Koneksi();
        db.config();
        this.stm = db.stm;
    }

    //method cekLogin(select)
    /*
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
     */
// Pake map biar bisa login 2 versi
    public Map<String, String> cekLogin(String un, String pw) {
        Map<String, String> loginData = null;
        this.res = null;

        try {
            // 1  LOGIN  ADMIN 
            this.sql = "SELECT id_admin AS id, nama FROM tb_admin WHERE nama = '" + un + "' AND password = '" + pw + "'";
            this.res = this.stm.executeQuery(sql);
            if (this.res.next()) {
                loginData = new HashMap<>();
                loginData.put("id", res.getString("id"));
                loginData.put("nama", res.getString("nama"));
                loginData.put("role", "Admin");
                return loginData;
            }

            //  2 LOGIN PEGAWAI 
            this.sql = "SELECT id_pegawai AS id, nama FROM tb_kasir WHERE nama = '" + un + "' AND password = '" + pw + "'";
            this.res = this.stm.executeQuery(sql);
            if (this.res.next()) {
                loginData = new HashMap<>();
                loginData.put("id", res.getString("id"));
                loginData.put("nama", res.getString("nama"));
                loginData.put("role", "Kasir");
                return loginData;
            }

            // Cek tb_mekanik
            this.sql = "SELECT id_pegawai AS id, nama FROM tb_mekanik WHERE nama = '" + un + "' AND password = '" + pw + "'";
            this.res = this.stm.executeQuery(sql);
            if (this.res.next()) {
                loginData = new HashMap<>();
                loginData.put("id", res.getString("id"));
                loginData.put("nama", res.getString("nama"));
                loginData.put("role", "Mekanik");
                return loginData;
            }
        } catch (Exception e) {
            System.out.println("Query Login Gagal: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //gaji semua
    public DefaultTableModel tampilkanGajiSemua() {
        DefaultTableModel dtmSemua = new DefaultTableModel();

        dtmSemua.addColumn("ID Karyawan");
        dtmSemua.addColumn("Nama");
        dtmSemua.addColumn("Gaji ");
        dtmSemua.addColumn("Status");

        try {
            // Hubungkan tb_pegawai dan tb_gaji
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

                dtmSemua.addRow(obj);
            }
            return dtmSemua; 

        } catch (Exception e) {
            System.out.println("Gagal query tampilkanGajiSemua: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //tampilkan gaji ptibadi
    public DefaultTableModel tampilkanGajiPribadi(String idPegawai) {
        DefaultTableModel dtmPribadi = new DefaultTableModel();


        dtmPribadi.addColumn("ID Karyawan");
        dtmPribadi.addColumn("Nama");
        dtmPribadi.addColumn("Gaji ");
        dtmPribadi.addColumn("Status");

        try {
            this.sql = "SELECT p.id_pegawai, p.nama, g.gaji, g.status_pengambilan_gaji"
                    + " FROM tb_pegawai p "
                    + "LEFT JOIN tb_gaji g ON p.id_pegawai = g.id_karyawan "
                    + "WHERE p.id_pegawai = '" + idPegawai + "' "
                    + "ORDER BY p.id_pegawai ASC";

            this.res = this.stm.executeQuery(sql);

            while (res.next()) {
                Object[] obj = new Object[4];
                obj[0] = res.getString("id_pegawai");
                obj[1] = res.getString("nama");
                obj[2] = res.getString("gaji");
                obj[3] = res.getString("status_pengambilan_gaji");

                dtmPribadi.addRow(obj);
            }
            return dtmPribadi;

        } catch (Exception e) {
            System.out.println("Gagal query tampilkanGajiPribadi: " + e.getMessage());
            e.printStackTrace();
            return null;
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

    public DefaultTableModel tampilkanSemuaPegawai() {
        DefaultTableModel dtmPegawai = new DefaultTableModel();

        dtmPegawai.addColumn("ID Pegawai");
        dtmPegawai.addColumn("Nama");
        dtmPegawai.addColumn("No. Telp");
        dtmPegawai.addColumn("Email");
        dtmPegawai.addColumn("Status");

        try {
            this.sql = "SELECT id_pegawai, nama, no_telp, email, status FROM tb_pegawai ORDER BY id_pegawai ASC";

            this.res = this.stm.executeQuery(sql);

            while (res.next()) {
                Object[] obj = new Object[5];
                obj[0] = res.getString("id_pegawai");
                obj[1] = res.getString("nama");
                obj[2] = res.getString("no_telp");
                obj[3] = res.getString("email");
                obj[4] = res.getString("status");

                dtmPegawai.addRow(obj);
            }
            return dtmPegawai;

        } catch (Exception e) {
            System.out.println("querry gagal: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public Pegawai getPegawaiDataPribadi(String idPegawai) {
    Pegawai pegawai = null;
    try {
        this.sql = "SELECT id_pegawai, no_telp, email FROM tb_pegawai WHERE id_pegawai = '" + idPegawai + "'";
        this.res = this.stm.executeQuery(sql);

        if (this.res.next()) {
            pegawai = new Pegawai();
            pegawai.setId_pegawai(this.res.getString("id_pegawai"));
            pegawai.setNo_telp(this.res.getString("no_telp"));
            pegawai.setEmail(this.res.getString("email"));
        }
    } catch (Exception e) {
        System.out.println("Gagal: " + e.getMessage());
    }
    return pegawai;
}

}
//semoga benar 
