package models.sample;

import java.util.Date;
import javax.persistence.*;

import io.ebean.*;
import play.data.validation.Constraints;
import play.data.format.Formats;


/**
 * アンケートデータのためのモデル
 *
 * アンケートデータは以下のパラメータを持つ
 * - id: アンケートID
 * - name: アンケート投稿者の名前
 * - sex: アンケート投稿者の性別
 * - impressions: 投稿された感想
 * - registeredAt: 投稿日時
 */
@Entity
@Table(name="enquetes")
public class Enquete extends Model {

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

    /**
     * アンケートID
     */
    @Id
    @GeneratedValue
    public long id;

    /**
     * 投稿者の名前
     */
    @Constraints.Required
    @Constraints.MaxLength(25)
    @Formats.NonEmpty
    @Column(nullable = false)
    public String name;

    /**
     * 投稿者の性別
     */
    @Constraints.Required
    @Constraints.MaxLength(10)
    @Formats.NonEmpty
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public SexTag sex;

    /**
     * 投稿された感想
     */
    @Constraints.Required
    @Constraints.MaxLength(255)
    @Formats.NonEmpty
    @Column(nullable = false)
    public String impressions;

    /**
     * 投稿日時
     */
    @Formats.DateTime(pattern="yyyy/MM/dd HH:mm:ss")
    @Formats.NonEmpty
    @Column(name = "registered_at", nullable = false)
    public Date registeredAt = new Date();

    /**
     * 更新日時
     */
    @Formats.DateTime(pattern="yyyy/MM/dd HH:mm:ss")
    @Formats.NonEmpty
    @Column(name = "updated_at", nullable = false)
    public Date updatedAt = null;

    /**
     * コンストラクタ：アンケートデータの新規作成
     * @param name アンケート投稿者の名前
     * @param sex アンケート投稿者の性別
     * @param impressions 投稿された感想
     */
    public Enquete(final String name, final SexTag sex, final String impressions) {
        this.name = name;
        this.sex = sex;
        this.impressions = impressions;
    }

    /**
     * モデル保存時に更新日時を更新
     */
    @Override
    public void save() {
        updatedAt = (updatedAt == null ? registeredAt : new Date());
        super.save();
    }

}
