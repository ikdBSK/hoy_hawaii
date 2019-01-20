package controllers;

import data.record.SchoolExamTime;
import data.record.Subject;
import data.record.TestResult;
import play.libs.Json;
import play.mvc.*;

import views.html.*;
import data.*;

import java.util.*;

public class DataController extends Controller {
    final private String default_password = "0000";
    final private String admin_id = "admin";

    Account admin = new Account(admin_id, default_password, null, null, null);//管理者アカウント
    List<Student> students = new ArrayList<Student>();
    List<Teacher> teachers = new ArrayList<Teacher>();
    List<Subject> subjects = new ArrayList<Subject>();

    //idが一致する生徒を検索
    public Student get_student(String id) {
        for(Student s : students){
            if(s.get_id().equals(id)){
                return s;
            }
        }
        return null;
    }

    //idが一致する教師を検索
    public Teacher get_teacher(String id) {
        for(Teacher t : teachers){
            if(t.get_id().equals(id)){
                return t;
            }
        }
        return null;
    }

    //生徒情報を更新
    public void update_student(Student student){
        students.forEach(s -> {
            if(s.get_id().equals(student.get_id())){
                students.set(students.indexOf(s), student);
            }
        });
    }

    //教師情報を更新
    public void update_teacher(Teacher teacher){
        teachers.forEach(t -> {
            if(t.get_id().equals(teacher.get_id())){
                teachers.set(teachers.indexOf(t), teacher);
            }
        });
    }

    //ログインするアカウントIDをsessionに保持し、そのsessionIdをクライアントのCookieに保持して紐付ける
    public void connect_session(String id) {
        final String sessionId = UUID.randomUUID().toString();
        session(sessionId, id);
        response().setCookie(
                Http.Cookie.builder("sessId", sessionId)
                        .build()
        );
    }

    //sessionとCookie削除
    public void disconnect_session() {
        final Http.Cookie sessionId = request().cookie("sessId");
        if(sessionId != null) {
            session().remove(sessionId.value());
            response().discardCookie("sessId");
        }
    }

    //sessionに保持したアカウントIDを確認
    public String get_id() {
        final Http.Cookie sessionId = request().cookie("sessId");
        if(sessionId == null || !session().containsKey(sessionId.value())){
            //ユーザー情報がなければタスクの表示が無いページ返す
            return null;
        }
        return session(sessionId.value());
    }




    /**
     * @return ログインページ
     */
    public Result login() {
        String account_id = get_id();
        if(account_id == null) return ok(index.render("NONE"));
        if(account_id.startsWith("S")) return ok(index.render("STUDENT"));
        if(account_id.startsWith("T")) return ok(index.render("TEACHER"));
        if(account_id.equals(admin_id)) return ok(index.render("ADMIN"));
        return unauthorized();
    }

    /**
     * @return 名前
     */
    public Result username() {
        String account_id = get_id();
        if(account_id == null) return unauthorized();
        if(account_id.startsWith("S")) return ok(Json.toJson(get_student(account_id).get_name()));
        if(account_id.startsWith("T")) return ok(Json.toJson(get_teacher(account_id).get_name()));
        if(account_id.equals(admin_id)) return ok(Json.toJson("管理者"));
        return badRequest();
    }


    /**
     * @return JSON形式のアカウントリスト
     */
    public Result account_list() {
        final String account_id = get_id();
        if(account_id == null || !account_id.equals(admin_id)) return badRequest();
        List<Account> accounts = new ArrayList<Account>();
        accounts.addAll(teachers);
        accounts.addAll(students);
        return ok(Json.toJson(accounts));
    }


    /**
     * @return JSON形式の生徒リスト
     */
    public Result student_list() {
        final String account_id = get_id();
        if(account_id == null) return badRequest();
        if(account_id.equals(admin_id)) return ok(Json.toJson(students));
        //if(get_teacher(account_id) != null) return ok(Json.toJson(get_teacher(account_id).get_subjectClass().getstudent());
        return badRequest();
    }


    /**
     * @return JSON形式の教師リスト
     */
    public Result teacher_list() {
        final String account_id = get_id();
        if(account_id == null || !account_id.equals(admin_id)) return badRequest();
        return ok(Json.toJson(teachers));
    }


    /**
     * 科目のリストを返す
     * @return JSON形式のSubjectリスト
     */
    public Result subject_list() {
        return ok(Json.toJson(subjects));
    }


    /**
     * idとpassword受け取って
     * アカウントのタイプに対応するページ返す
     * @return ログイン後のトップページ
     */
    public Result authenticate() {
        //リクエストbodyからidとpasswordを受け取る
        final Map<String, String[]> request = request().body().asFormUrlEncoded();
        final String id = request.get("id")[0];
        final String password = request.get("password")[0];

        //生徒のidはSで始まり教師のidはTで始まることを想定
        if(id.startsWith("S")){
            //idが一致するアカウント取得
            final Student student = get_student(id);
            //idかpasswordが一致しないとき
            if(student == null || !student.check_password(password)){
                return unauthorized();
            }
            connect_session(id);
            //生徒情報を引数として付加した生徒用のトップページを返す
            return ok(Json.toJson("STUDENT"));
        }

        if(id.startsWith("T")){
            final Teacher teacher = get_teacher(id);
            if(teacher == null || !teacher.check_password(password)){
                return unauthorized();
            }
            connect_session(id);
            return ok(Json.toJson("TEACHER"));
        }

        //管理者アカウントと一致しないとき
        if(!admin.get_id().equals(id) || !admin.check_password(password)){
            return unauthorized();
        }
        connect_session(id);
        //debug用にstudentとteacher登録
        Student s1 = new Student("S0001", default_password, "生徒1", Account.SexTag.male, "A県B市1-1-1");
        students.add(s1);
        Teacher t1 = new Teacher("T0001", default_password, "教師1", Account.SexTag.female, "C県D市1-1-1");
        teachers.add(t1);
        return ok(Json.toJson("ADMIN"));
    }


    /**
     * ログアウトする
     * @return ログインページ
     */
    public Result logout() {
        disconnect_session();
        return redirect("/");
    }


    /**
     * 管理者ページからidとpassword受け取って
     * アカウント新規登録する
     * @return 新規登録後のアカウントリスト
     */
    public Result signup() {
        try {
            //管理者からのアクセスであることを確認
            final String account_id = get_id();
            if(account_id == null || !account_id.equals(admin_id)) return badRequest();
            Map<String, String[]> form = request().body().asFormUrlEncoded();
            final String id = form.get("id")[0];
            final String password = form.get("password")[0];
            final String name = form.get("name")[0];
            final Account.SexTag sex = Account.SexTag.valueOf(form.get("sex")[0]);
            final String address = form.get("address")[0];
            //生徒のidはS、教師のidはTから始まることを想定
            if(id.startsWith("S")){
                if(get_student(id) != null) return unauthorized();
                final Student student = new Student(id, password, name, sex, address);
                students.add(student);
                return student_list();
            } else if(id.startsWith("T")){
                if(get_teacher(id) != null) return unauthorized();
                final Teacher teacher = new Teacher(id, password, name, sex, address);
                teachers.add(teacher);
                return teacher_list();
            } else {
                return badRequest();
            }
        }catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * アカウント情報を編集する
     * @param id 編集するアカウントのid
     * @return 編集後のアカウントリスト
     */
    public Result edit(String id) {
        try{
            final String account_id = get_id();
            if(account_id == null || !account_id.equals(admin_id)) return badRequest();
            Map<String, String[]> form = request().body().asFormUrlEncoded();
            if(id.startsWith("S")){
                Student student = get_student(id);
                if(student == null) return unauthorized();
                student.set_password(form.get("password")[0]);
                student.set_address(form.get("address")[0]);
                update_student(student);
                return student_list();
            }
            if(id.startsWith("T")){
                Teacher teacher = get_teacher(id);
                if(teacher == null) return unauthorized();
                teacher.set_password(form.get("password")[0]);
                teacher.set_address(form.get("address")[0]);
                update_teacher(teacher);
                return teacher_list();
            }
            if(id.equals(admin_id)){
                admin.set_password(form.get("password")[0]);
            }
            return ok(Json.toJson(0));
        }catch (Exception e){
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * 特定のアカウントのみを返す
     * @param id　返して欲しいアカウントのID
     * @return JSON形式でアカウントを返す
     */
    public Result fetch(String id) {
        try {
            //管理者からのアクセスのとき
            final String account_id = get_id();
            if(account_id == null || !account_id.equals(admin_id)) return badRequest();
            if(id.startsWith("S")){
                Student student = get_student(id);
                if(student != null) return ok(Json.toJson(student));
            }
            if(id.startsWith("T")){
                Teacher teacher = get_teacher(id);
                if(teacher != null) return ok(Json.toJson(teacher));
            }
            if(id.equals(admin_id)){
                return ok(Json.toJson(admin));
            }
            return notFound();
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }


/* ****************** 以下、生徒からのアクセスに対処するメソッド ************************* */

    /**
     * 自身のアカウントの定期テスト一覧を返す
     * @return JSON形式でテスト情報のリストを返す
     */
    public Result school_exam_list() {
        try {
            //生徒からのアクセスのみ処理
            final String account_id = get_id();
            if(account_id == null) return badRequest();
            Student student = get_student(account_id);
            if(student == null) return badRequest();
            List<SchoolExamTime> list = new ArrayList<>(student.getRecord().getExams().keySet());
            return ok(Json.toJson(list));
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * 自身のアカウントの特定の定期テスト結果を返す
     * @return JSON形式でTestResultのリストを返す
     */
    public Result school_exam_detail(int year, int semester, int term) {
        try {
            //生徒からのアクセスのみ処理
            final String account_id = get_id();
            if(account_id == null) return badRequest();
            Student student = get_student(account_id);
            if(student == null) return badRequest();
            Set<SchoolExamTime> time = student.getRecord().getExams().keySet();
            //year,semester,termが一致するSchoolExamTimeの成績一覧を探す
            for(SchoolExamTime t : time){
                if(t.getYear()==year && t.getSemester()==semester && t.getTerm()==term){
                    return ok(Json.toJson(student.getRecord().getExam(t)));
                }
            }
            return notFound();
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * 自身のアカウントのある科目の結果一覧を返す
     * @return JSON形式でTestResultのリストを返す
     */
    public Result subject_history(String name){
        try {
            //生徒からのアクセスのみ処理
            final String account_id = get_id();
            if(account_id == null) return badRequest();
            Student student = get_student(account_id);
            if(student == null) return badRequest();
            //nameが一致するSubjectの成績一覧を探す
            for(Subject s : subjects){
                if(s.getName().equals(name)){
                    return ok(Json.toJson(student.getRecord().getExam(s)));
                }
            }
            return notFound();
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }
}
