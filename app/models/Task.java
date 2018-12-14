package models;

import java.util.Date;
import javax.persistence.*;

import io.ebean.*;
import play.data.validation.Constraints;
import play.data.format.Formats;


/**
 * TODOリスト用タスクモデル
 */
@Entity
@Table(name="tasks")
public class Task extends Model {

    /**
     * タスクのID
     * 自動生成される
     */
    @Id
    @GeneratedValue
    public long id;

    /**
     * タスクのタイトル
     */
    @Constraints.Required
    @Constraints.MaxLength(255)
    @Formats.NonEmpty
    @Column(nullable = false)
    public String title;

    /**
     * タスク完了状態
     */
    @Constraints.Required
    @Formats.NonEmpty
    @Column(nullable = false)
    public boolean done = false;

    /**
     * タスク登録タイムスタンプ
     * 作成時に自動的に初期化される
     */
    @Formats.DateTime(pattern="yyyy/MM/dd HH:mm:ss")
    @Formats.NonEmpty
    @Column(name = "registered_at", nullable = false)
    public Date registeredAt = new Date();


    /**
     * タスクの作成
     * @param title タイトルは必須
     */
    public Task(String title) {
        this.title = title;
    }

}
