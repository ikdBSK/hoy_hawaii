package data;

import org.apache.commons.codec.digest.DigestUtils;

public class Account {
    /**
     * 性別用のタグ
     */
    public enum SexTag {
        male("男性"), female("女性");

        private final String displayValue;

        SexTag(final String displayValue) {
            this.displayValue = displayValue;
        }

        public String display() {
            return this.displayValue;
        }
    }

    private String id;
    private String digested_password;
    private String name;
    private SexTag sex;
    private String address;

    //コンストラクタ
    public Account(String id, String password, String name, SexTag sex, String address){
        this.id = id;
        this.digested_password = DigestUtils.sha256Hex(password);
        this.name = name;
        this.sex = sex;
        this.address = address;
    }

    //getter
    public String get_id() {
        return id;
    }
    public String get_password() {
        return digested_password;
    }
    public String get_name() {
        return name;
    }
    public SexTag get_sex() {
        return sex;
    }
    public String get_address() {
        return address;
    }

    //setter
    public void set_password(String password) {
        this.digested_password = DigestUtils.sha256Hex(password);
    }
    public void set_address(String address) {
        this.address = address;
    }

    //password一致判定
    public boolean check_password(String password) {
        return this.digested_password.equals(DigestUtils.sha256Hex(password));
    }
}
