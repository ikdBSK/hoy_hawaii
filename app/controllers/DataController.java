package controllers;

import data.record.*;
import play.libs.Json;
import play.mvc.*;

import views.html.*;
import data.*;

import java.util.*;

public class DataController extends Controller {
    final private String default_password = "0000";
    final private String admin_id = "admin";

    private Account admin = new Account(admin_id, default_password, null, null, null);//管理者アカウント
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Teacher> teachers = new ArrayList<>();
    private ArrayList<Subject> subjects = new ArrayList<>();
    private ArrayList<SchoolExam> exams = new ArrayList<>();

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
        List<Account> accounts = new ArrayList<>();
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
            return ok(index.render("STUDENT"));
        }

        if(id.startsWith("T")){
            final Teacher teacher = get_teacher(id);
            if(teacher == null || !teacher.check_password(password)){
                return unauthorized();
            }
            connect_session(id);
            return ok(index.render("TEACHER"));
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
        return ok(index.render("ADMIN"));
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


    /* ****************** 以下、管理者からのアクセスに対処するメソッド ************************* */

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
     * 学校の試験一回のオブジェクトを作る
     * @return SchoolExamオブジェクト一覧
     */
    public Result make_school_exam() {
        try{
            final String account_id = get_id();
            if(account_id == null || !account_id.equals(admin_id)) return badRequest();
            Map<String, String[]> form = request().body().asFormUrlEncoded();
            final int year = Integer.parseInt(form.get("year")[0]);
            final int semester = Integer.parseInt(form.get("semester")[0]);
            final int term = Integer.parseInt(form.get("term")[0]);
            SchoolExamTime time = new SchoolExamTime(year, semester, term);
            SchoolExam exam = new SchoolExam(time);
            exams.add(exam);
            return ok(Json.toJson(exams));
        }catch(Exception e){
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * 一つの科目のインスタンスを作る
     * @return Subjectリスト
     */
    public Result add_subject(){
        try{
            final String account_id = get_id();
            if(account_id == null || !account_id.equals(admin_id)) return badRequest();
            Map<String, String[]> form = request().body().asFormUrlEncoded();
            final String name = form.get("name")[0];
            final int credits = Integer.parseInt(form.get("credit")[0]);
            Subject subject = new Subject(name, credits);
            subjects.add(subject);
            return ok(Json.toJson(subjects));
        }catch(Exception e){
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
            for(SchoolExamTime temp : list){
                school_exam_list_table send = new school_exam_list_table();
                send.term = (0 == temp.getTerm()) ? "中間" : "期末";
            }
            return ok(Json.toJson(list));
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }

    class school_exam_list_table{
        public int year;
        public int semester;
        public String term;
        public int total;
        public int rank;
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


    /* ****************** 以下、教師からのアクセスに対処するメソッド ************************* */

    /**
     * 教師が、自身の担当している科目クラス一覧を得る
     * @return SubjectClassのリスト
     */
    public Result subject_class_list(){
        final String account_id = get_id();
        if(account_id == null) return badRequest();
        Teacher teacher = get_teacher(account_id);
        if(teacher == null) return badRequest();
        return ok(Json.toJson(teacher.getClasses()));
    }


    /**
     * 特定の科目クラスの試験一覧(中間、期末とか)を返す
     * @return SchoolTestのリスト
     */
    public Result school_test_list(int year, int semester, String name, int grade){
        try {
            final String account_id = get_id();
            if(account_id == null) return badRequest();
            Teacher teacher = get_teacher(account_id);
            if(teacher == null) return badRequest();
            //teacherの持つSubjectClassリストから科目名・年・学期が一致するものを特定
            for(SubjectClass c : teacher.getClasses()){
                if(c.getSubject().getName().equals(name) && c.getGrade() == grade
                        && c.getSemester().getYear() == year && c.getSemester().getSemester() == semester){
                    return ok(Json.toJson(c.getTests()));
                }
            }
            return notFound();
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * 特定のSchoolTestの生徒の試験結果一覧を返す
     * @param year 実施した年
     * @param semester 実施した学期
     * @param term 中間/期末とか
     * @param name 科目名
     * @param grade 学年
     * @return TestResultのリスト
     */
    public Result students_results(int year, int semester, int term, String name, int grade){
        try {
            final String account_id = get_id();
            if(account_id == null) return badRequest();
            Teacher teacher = get_teacher(account_id);
            if(teacher == null) return badRequest();

            SubjectClass subjectClass = null;
            //teacherの持つSubjectClassリストから科目名・年・学期が一致するものを特定
            for(SubjectClass c : teacher.getClasses()){
                if(c.getSubject().getName().equals(name) && c.getGrade() == grade
                        && c.getSemester().getYear() == year && c.getSemester().getSemester() == semester){
                    subjectClass = c;
                    break;
                }
            }
            if(subjectClass == null) return notFound();
            SchoolTest test = null;
            //subjectClassの持つSchoolTestリストの中から時期の一致するものを特定
            for(SchoolTest t : subjectClass.getTests()){
                SchoolExamTime time = t.getExam().getTime();
                if(time.getYear() == year && time.getSemester() == semester && time.getTerm() == term){
                    test = t;
                    break;
                }
            }
            if(test == null) return notFound();
            ArrayList<TestResult> testResults = new ArrayList<>(test.getResult().values());
            return ok(Json.toJson(testResults));
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * 教師が試験実施日を確定したらその情報を持つSchoolTestを作っておく
     * @param year 実施した年
     * @param semester 実施した学期
     * @param term 中間/期末とか
     * @param name 科目名
     * @param grade 学年
     * @return 作成したSchoolTestが追加された、subjectClassのtestsリスト
     */
    public Result add_test(int year, int semester, int term, String name, int grade){
        try {
            final String account_id = get_id();
            if(account_id == null) return badRequest();
            Teacher teacher = get_teacher(account_id);
            if(teacher == null) return badRequest();

            SubjectClass subjectClass = null;
            //teacherの持つSubjectClassリストから科目名・年・学期が一致するものを特定
            for(SubjectClass c : teacher.getClasses()){
                if(c.getSubject().getName().equals(name) && c.getGrade() == grade
                        && c.getSemester().getYear() == year && c.getSemester().getSemester() == semester){
                    subjectClass = c;
                    break;
                }
            }
            if(subjectClass == null) return notFound();
            SchoolExam exam = null;
            //管理者が作ったSchoolExamリストの中から時期の一致するものを特定
            for(SchoolExam e : exams){
                SchoolExamTime time = e.getTime();
                if(time.getYear() == year && time.getSemester() == semester && time.getTerm() == term){
                    exam = e;
                    break;
                }
            }
            if(exam == null) return notFound();
            Map<String, String[]> form = request().body().asFormUrlEncoded();
            final int month = Integer.parseInt(form.get("month")[0]);
            final int day = Integer.parseInt(form.get("day")[0]);
            final int division = Integer.parseInt(form.get("division")[0]);
            SchoolTime schoolTime = new SchoolTime(year, month, day, division);
            //目的のインスタンス作成 (作成後はSchoolExamやSubjectClassから参照できる)
            new SchoolTest(exam, schoolTime, subjectClass);
            return ok(Json.toJson(subjectClass.getTests()));
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * 一人の生徒の試験結果を登録する
     * @param year 実施した年
     * @param semester 実施した学期
     * @param term 中間/期末とか
     * @param name 科目名
     * @param grade 学年
     * @return TestResultのリスト
     */
    public Result add_result(int year, int semester, int term, String name, int grade){
        try {
            final String account_id = get_id();
            if(account_id == null) return badRequest();
            Teacher teacher = get_teacher(account_id);
            if(teacher == null) return badRequest();

            SubjectClass subjectClass = null;
            //teacherの持つSubjectClassリストから科目名・年・学期が一致するものを特定
            for(SubjectClass c : teacher.getClasses()){
                if(c.getSubject().getName().equals(name) && c.getGrade() == grade
                        && c.getSemester().getYear() == year && c.getSemester().getSemester() == semester){
                    subjectClass = c;
                    break;
                }
            }
            if(subjectClass == null) return notFound();
            SchoolTest test = null;
            //subjectClassの持つSchoolTestリストの中から時期の一致するものを特定
            for(SchoolTest t : subjectClass.getTests()){
                SchoolExamTime time = t.getExam().getTime();
                if(time.getYear() == year && time.getSemester() == semester && time.getTerm() == term){
                    test = t;
                    break;
                }
            }
            if(test == null) return notFound();
            //とりあえず点数と生徒IDの受け取りのみ実装
            Map<String, String[]> form = request().body().asFormUrlEncoded();
            int score = Integer.parseInt(form.get("score")[0]);
            String student_id = form.get("id")[0];
            Student student = get_student(student_id);
            if(student == null) return notFound();
            TestResult result = new TestResult(score, student, subjectClass);
            test.addResult(result);

            ArrayList<TestResult> testResults = new ArrayList<>(test.getResult().values());
            return ok(Json.toJson(testResults));
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }
}
