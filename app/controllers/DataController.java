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
    private ArrayList<Grade> grades = new ArrayList<>();
    private ArrayList<ExternalExamType> ex_types = new ArrayList<>();
    private ArrayList<ExternalExam> ex_exams = new ArrayList<>();

    {
        test();
    }

    private void test(){

    }

    //idが一致する生徒を検索
    private Student get_student(String id) {
        for(Student s : students){
            if(s.get_id().equals(id)){
                return s;
            }
        }
        return null;
    }

    //idが一致する教師を検索
    private Teacher get_teacher(String id) {
        for(Teacher t : teachers){
            if(t.get_id().equals(id)){
                return t;
            }
        }
        return null;
    }

    //nameが一致するSubjectを検索
    private Subject get_subject(String name) {
        for(Subject s : subjects){
            if(s.getName().equals(name)){
                return s;
            }
        }
        return null;
    }

    //year, semester, term が一致する試験を検索
    private SchoolExam get_school_exam(int year, int semester, int term) {
        for(SchoolExam e : exams){
            SchoolExamTime time = e.getTime();
            if(time.getYear() == year && time.getSemester() == semester && time.getTerm() == term){
                return e;
            }
        }
        return null;
    }

    //teacherの持つSubjectClassリストから科目名・学年・年・学期が一致するものを特定
    private SubjectClass get_subject_class(Teacher teacher, String name, int grade, int year, int semester) {
        for(SubjectClass c : teacher.getClasses()){
            if(c.getSubject().getName().equals(name) && c.getGrade() == grade
                    && c.getSemester().getYear() == year && c.getSemester().getSemester() == semester){
                return c;
            }
        }
        return null;
    }

    //subjectClassの持つSchoolTestリストの中から時期の一致するものを特定
    private SchoolTest get_school_test(SubjectClass subjectClass, int year, int semester, int term) {
        for(SchoolTest t : subjectClass.getTests()){
            SchoolExamTime time = t.getExam().getTime();
            if(time.getYear() == year && time.getSemester() == semester && time.getTerm() == term){
                return t;
            }
        }
        return null;
    }

    //yearとgradeが一致するGradeを検索
    private Grade get_grade(int year, int grade){
        for(Grade g : grades){
            if(g.getYear() == year && g.getGrade() == grade){
                return g;
            }
        }
        return null;
    }

    //nameの一致する模試タイプを検索
    private ExternalExamType get_ex_type(String name) {
        for(ExternalExamType type : ex_types){
            if(type.getName().equals(name)) return type;
        }
        return null;
    }

    //year, month, day, type が一致する模試を検索
    private ExternalExam get_ex_exam(int year, int month, int day, String type) {
        ExternalExamType ex_type = get_ex_type(type);
        for(ExternalExam e : ex_exams){
            ExternalTime time = e.getTime();
            if(time.getYear()==year && time.getMonth()==month && time.getDay()==day && time.getType().equals(ex_type)){
                return e;
            }
        }
        return null;
    }

    //時期、模試名、科目名の一致するExternalTestを特定
    private ExternalTest get_external_test(int year, int month, int day, String type, String subject_name){
        ExternalExam exam = get_ex_exam(year, month, day, type);
        if (exam != null) {
            for(ExternalTest t : exam.getTests()){
                String name = t.getSubject().getName();
                if(name.equals(subject_name)){
                    return t;
                }
            }
        }
        return null;
    }

    //生徒情報を更新
    private void update_student(Student student){
        students.forEach(s -> {
            if(s.get_id().equals(student.get_id())){
                students.set(students.indexOf(s), student);
            }
        });
    }

    //教師情報を更新
    private void update_teacher(Teacher teacher){
        teachers.forEach(t -> {
            if(t.get_id().equals(teacher.get_id())){
                teachers.set(teachers.indexOf(t), teacher);
            }
        });
    }

    //ログインするアカウントIDをsessionに保持し、そのsessionIdをクライアントのCookieに保持して紐付ける
    private void connect_session(String id) {
        final String sessionId = UUID.randomUUID().toString();
        session(sessionId, id);
        response().setCookie(
                Http.Cookie.builder("sessId", sessionId)
                        .build()
        );
    }

    //sessionとCookie削除
    private void disconnect_session() {
        final Http.Cookie sessionId = request().cookie("sessId");
        if(sessionId != null) {
            session().remove(sessionId.value());
            response().discardCookie("sessId");
        }
    }

    //sessionに保持したアカウントIDを確認
    private String get_id() {
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
        try {
            String account_id = get_id();
            if (account_id == null) return ok(Json.toJson("NULL :)"));
            if (account_id.startsWith("S")) return ok(Json.toJson(Objects.requireNonNull(get_student(account_id)).get_name()));
            if (account_id.startsWith("T")) return ok(Json.toJson(Objects.requireNonNull(get_teacher(account_id)).get_name()));
            if (account_id.equals(admin_id)) return ok(Json.toJson("管理者"));
        } catch(Exception e){
            return badRequest();
        }
        return notFound();
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

        class TMPAccount{
            public final String id;
            public final String name;
            public final String sex;
            public final String address;

            private TMPAccount(String id, String name, String sex, String address){
                this.id = id;
                this.name = name;
                this.sex = sex;
                this. address = address;
            }
        }

        ArrayList<TMPAccount> tmp = new ArrayList<>();
        for(Account account : accounts){
            tmp.add(new TMPAccount(account.get_id(), account.get_name(), account.get_sex().display(), account.get_address()));
        }
        return ok(Json.toJson(tmp));
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
        class TMPSubject {
            public final String name;
            public final int credits;

            private TMPSubject(Subject s){
                name = s.getName();
                credits = s.getCredits();
            }
        }

        ArrayList<TMPSubject> temp = new ArrayList<>();
        for(Subject s : subjects){
            temp.add(new TMPSubject(s));
        }

        return ok(Json.toJson(temp));
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
     * 特定のアカウントのみを返す
     * @param id　返して欲しいアカウントのID
     * @return JSON形式でアカウントを返す
     */
    public Result fetch(String id) {
        try {
            //管理者からのアクセスのとき
            final String account_id = get_id();
            if(account_id == null || !account_id.equals(admin_id)) return badRequest();

            class TMPAccount {
                public final String id;
                public final String name;
                public final String sex;
                public final String address;
                public final String password;

                private TMPAccount(Account account){
                    this.id = account.get_id();
                    this.name = account.get_name();
                    this.sex = account.get_sex().toString();
                    this.address = account.get_address();
                    this.password = account.get_password();
                }
            }

            if(id.startsWith("S")){
                Student student = get_student(id);
                TMPAccount temp = new TMPAccount(Objects.requireNonNull(student));
                return ok(Json.toJson(temp));
            }
            if(id.startsWith("T")){
                Teacher teacher = get_teacher(id);
                TMPAccount temp = new TMPAccount(Objects.requireNonNull(teacher));
                return ok(Json.toJson(temp));
            }
            if(id.equals(admin_id)){
                TMPAccount temp = new TMPAccount(admin);
                return ok(Json.toJson(temp));
            }
            return notFound();
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * ある生徒の特定の試験の総合点を返す
     * @param student 生徒
     * @param time 試験の時期
     * @return 総合点
     */
    private int get_total(Student student, SchoolExamTime time){
        return  student.getRecord().getTotalScore(time);
    }


    /**
     * ある生徒の特定の試験での総合順位を返す
     * @param student 生徒
     * @param time 試験の時期
     * @return 総合順位
     */
    private int get_rank(Student student, SchoolExamTime time){
        final ClassRoom classRoom = student.getClassRoom().get(time.getYear());
        return classRoom.getRank(student, time);
    }


    /**
     * ある生徒の特定の試験での科目の順位を返す
     * @param student 生徒
     * @param time 試験の時期
     * @param subject 科目
     * @return 順位
     */
    private int get_rank(Student student, SchoolExamTime time, Subject subject){
        return student.getRecord().getRank(time, subject);
    }


    /**
     * 指定された回の得点率(%)を返す
     * @param time 返す回
     * @return 得点率
     */
    private double get_rate(Student student, SchoolExamTime time){
        return student.getRecord().getRate(time);
    }


    /**
     * 得点率基準での偏差値を返す
     * @param student 偏差値を返す生徒
     * @param time 返す回
     * @return 偏差値
     */
    private double get_d_value(Student student, SchoolExamTime time){
        final ClassRoom classRoom = student.getClassRoom().get(time.getYear());
        return classRoom.getDValue(student, time);
    }


    /**
     * 指定された回、科目の偏差値を返す
     */
    private double get_d_value(Student student, SchoolExamTime time, Subject subject){
        return student.getRecord().getDValue(time, subject);
    }


    /**
     * ある生徒の特定の模試の総合点を返す
     * @param student 生徒
     * @param time 模試の時期
     * @return 総合点
     */
    public int get_ex_total(Student student, ExternalTime time){
        return student.getExRecord().getTotalScore(time);
    }


    /**
     * 指定された模試の得点率(%)を返す
     * @param time 返す回
     * @return 得点率
     */
    public double get_ex_rate(Student student, ExternalTime time){
        return student.getExRecord().getRate(time);
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
                return ok();
            } else if(id.startsWith("T")){
                if(get_teacher(id) != null) return unauthorized();
                final Teacher teacher = new Teacher(id, password, name, sex, address);
                teachers.add(teacher);
                return ok();
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
                student.set_name(form.get("name")[0]);
                student.set_sex(form.get("sex")[0]);
                update_student(student);
                return ok();
            }
            if(id.startsWith("T")){
                Teacher teacher = get_teacher(id);
                if(teacher == null) return unauthorized();
                teacher.set_password(form.get("password")[0]);
                teacher.set_address(form.get("address")[0]);
                teacher.set_name(form.get("name")[0]);
                teacher.set_sex(form.get("sex")[0]);
                update_teacher(teacher);
                return ok();
            }
            if(id.equals(admin_id)){
                admin.set_password(form.get("password")[0]);
            }
            return ok();
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
            exam.release();
            exams.add(exam);
            return ok();
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
            final int credits = Integer.parseInt(form.get("credits")[0]);
            Subject subject = new Subject(name, credits);
            subjects.add(subject);
            return ok();
        }catch(Exception e){
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * 選んだ科目の科目クラス（担当教師・生徒・開講時期などの組）のリストを返す
     * @param name 選んだ科目の名前
     * @return SubjectClassリスト
     */
    public Result subject_detail(String name){
        Subject subject = get_subject(name);
        if(subject == null) return notFound();

        class TMPSubjectClass {
            public final String subject;
            public final String teacher;
            public final int grade;
            public final int year;
            public final int semester;

            private TMPSubjectClass(SubjectClass t){
                subject = t.getSubject().getName();
                teacher = t.getTeacher().get_name();
                grade = t.getGrade();
                year = t.getSemester().getYear();
                semester = t.getSemester().getSemester();
            }
        }

        ArrayList<SubjectClass> classes = subject.getClasses();
        ArrayList<TMPSubjectClass> temp = new ArrayList<>();
        for(SubjectClass s : classes){
            temp.add(new TMPSubjectClass(s));
        }

        return ok(Json.toJson(temp));
    }


    /**
     * 科目クラスSubjectClassを作る
     * @return 同じ名前の科目クラス一覧
     */
    public Result make_subject_class(){
        try{
            final String account_id = get_id();
            if(account_id == null || !account_id.equals(admin_id)) return badRequest();
            Map<String, String[]> form = request().body().asFormUrlEncoded();
            final String subject_name = form.get("subject_name")[0];
            final String teacher_id = form.get("teacher_id")[0];
            final String[] students_ids = form.get("students_ids")[0].split(",");
            final int year = Integer.parseInt(form.get("year")[0]);
            final int semester = Integer.parseInt(form.get("semester")[0]);
            final int grade = Integer.parseInt(form.get("grade")[0]);
            //subjectを特定する
            Subject subject = get_subject(subject_name);
            if(subject == null) return notFound();
            //teacherを特定する
            Teacher teacher = get_teacher(teacher_id);
            if(teacher == null) return notFound();
            //studentsを特定する
            ArrayList<Student> student_list = new ArrayList<>();
            for (String students_id : students_ids) {
                Student student = get_student(students_id);
                if (student != null) {
                    student_list.add(student);
                }
            }
            //SchoolSemesterインスタンスを作る
            SchoolSemester schoolSemester = new SchoolSemester(year, semester);
            //SubjectClass登録
            new SubjectClass(subject, teacher, student_list, schoolSemester, grade);
            return ok();
        }catch(Exception e){
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * 指定された年のクラスルームのリストを返す
     * @param year 学年
     * @return ClassRoomリスト
     */
    public Result classroom_list(int year){
        ArrayList<ClassRoom> classRooms = new ArrayList<>();
        for(Grade grade : grades){
            if(grade.getYear() == year){
                classRooms.addAll(grade.getClassRooms());
            }
        }
        return ok(Json.toJson(classRooms));
    }


    /**
     * 新しいクラスルームを作る
     * @return 作ったのと同じ年のClassRoom一覧
     */
    public Result make_classroom(){
        try{
            final String account_id = get_id();
            if(account_id == null || !account_id.equals(admin_id)) return badRequest();
            Map<String, String[]> form = request().body().asFormUrlEncoded();
            int year = Integer.parseInt(form.get("year")[0]);
            final int grade_num = Integer.parseInt(form.get("grade")[0]);
            final int class_num = Integer.parseInt(form.get("class")[0]);
            final String teacher_id = form.get("teacher")[0];
            final String[] students_ids = form.get("students_ids")[0].split(",");
            Grade grade = get_grade(year, grade_num);
            //Gradeインスタンスがなければ作る
            if(grade == null){
                Grade grade1 = new Grade(1, year);
                Grade grade2 = new Grade(2, year);
                Grade grade3 = new Grade(3, year);
                grades.add(grade1);
                grades.add(grade2);
                grades.add(grade3);
            }
            //teacherを特定する
            Teacher teacher = get_teacher(teacher_id);
            if(teacher == null) return notFound();
            //studentsを特定する
            ArrayList<Student> student_list = new ArrayList<>();
            for(String s : students_ids){
                Student student = get_student(s);
                if(student != null){
                    student_list.add(student);
                }
            }
            //ClassRoomインスタンス作成
            new ClassRoom(teacher, student_list, grade, year);
            return ok();
        }catch(Exception e){
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * ExternalExamインスタンスを作る
     * 全科目のExternalTestインスタンスを作る
     * @return ExternalExamオブジェクト一覧
     */
    public Result make_external_exam() {
        try{
            final String account_id = get_id();
            if(account_id == null || !account_id.equals(admin_id)) return badRequest();
            Map<String, String[]> form = request().body().asFormUrlEncoded();
            final int year = Integer.parseInt(form.get("year")[0]);
            final int month = Integer.parseInt(form.get("month")[0]);
            final int day = Integer.parseInt(form.get("day")[0]);
            final String type = form.get("type")[0];
            ExternalExamType ex_type = get_ex_type(type);
            if(ex_type == null){
                ex_type = new ExternalExamType(type);
                ex_types.add(ex_type);
            }
            ExternalTime time = new ExternalTime(year, month, day, ex_type);
            ExternalExam exam = new ExternalExam(time, ex_type);
            exam.release();
            ex_exams.add(exam);
            //全科目のExternalTestインスタンスを作る
            for(Subject s : subjects){
                new ExternalTest(exam, time, s, ex_type);
            }
            return ok();
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



            class TMPSchoolExam{
                public int year;
                public int semester;
                public String term;
                public int total;
                public double rate;
                public double d_value;
                public int rank;

                private TMPSchoolExam(SchoolExamTime t, int total, double rate, double d_value, int rank){
                    year = t.getYear();
                    semester = t.getSemester();
                    term = (0 == t.getTerm()) ? "中間" : "期末";
                    this.total = total;
                    this.rate = rate;
                    this.d_value = d_value;
                    this.rank = rank;
                }
            }

            ArrayList<TMPSchoolExam> tmp = new ArrayList<>();
            for(SchoolExamTime t : list){
                tmp.add(new TMPSchoolExam(
                        t,
                        get_total(student, t),
                        get_rate(student, t),
                        get_d_value(student, t),
                        get_rank(student, t)
                ));
            }
            return ok(Json.toJson(tmp));
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }

    class TMPTestResult {
        public final String subject;
        public final int score;
        public final double d_value;
        public final int rank;
        public final int year;
        public final int semester;
        public final String term;

        private TMPTestResult(TestResult r, double d_value, int rank){
            subject = r.getSubject().getSubject().getName();
            score = r.getScore();
            this.d_value = d_value;
            this.rank = rank;
            year = r.getTime().getYear();
            semester = r.getTime().getSemester();
            term = (r.getTime().getTerm() == 0) ? "中間" : "期末";
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
            ArrayList<TestResult> exam = null;
            SchoolExamTime exam_time = null;
            for(SchoolExamTime t : time){
                if(t.getYear()==year && t.getSemester()==semester && t.getTerm()==term){
                    exam = student.getRecord().getExam(t);
                    exam_time = t;
                }
            }



            if(exam == null) return notFound();
            ArrayList<TMPTestResult> tmp = new ArrayList<>();
            for(TestResult r : exam){
                tmp.add(new TMPTestResult(
                   r,
                   get_d_value(student, exam_time, r.getSubject().getSubject()),
                   get_rank(student, exam_time, r.getSubject().getSubject())
                ));
            }

            return ok(Json.toJson(tmp));
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
            Subject subject = get_subject(name);
            if(subject == null) return notFound();

            ArrayList<TMPTestResult> tmp = new ArrayList<>();
            for(TestResult r : student.getRecord().getExam(subject)){
                tmp.add(new TMPTestResult(
                        r,
                        get_d_value(student, r.getTime(), r.getSubject().getSubject()),
                        get_rank(student, r.getTime(), r.getSubject().getSubject())
                ));
            }

            return ok(Json.toJson(tmp));
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * 自身の受けた模試一覧を返す
     * @return ExternalExamリストを返す
     */
    public Result my_ex_exams() {
        try {
            final String account_id = get_id();
            if(account_id == null) return badRequest();
            Student student = get_student(account_id);
            if(student == null) return badRequest();
            List<ExternalTime> list = new ArrayList<>(student.getExRecord().getExams().keySet());
            return ok(Json.toJson(list));
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * 自身の特定の模試の結果を返す
     * @return JSON形式でExternalTestResultのリストを返す
     */
    public Result ex_exam_detail(int year, int month, int day, String type) {
        try {
            final String account_id = get_id();
            if(account_id == null) return badRequest();
            Student student = get_student(account_id);
            if(student == null) return badRequest();
            ExternalExamType ex_type = get_ex_type(type);
            Set<ExternalTime> time = student.getExRecord().getExams().keySet();
            //year,month,day,typeが一致するExternalTimeを特定
            for(ExternalTime t : time){
                if(t.getYear()==year && t.getMonth()==month && t.getDay()==day && t.getType().equals(ex_type)){
                    return ok(Json.toJson(student.getExRecord().getExam(t)));
                }
            }
            return notFound();
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * 自身のある模試タイプのある科目の結果一覧を返す
     * @return JSON形式でExternalTestResultのリストを返す
     */
    public Result ex_subject_history(String type, String subject_name){
        try {
            //生徒からのアクセスのみ処理
            final String account_id = get_id();
            if(account_id == null) return badRequest();
            Student student = get_student(account_id);
            if(student == null) return badRequest();
            //nameが一致するSubjectの成績一覧を探す
            Subject subject = get_subject(subject_name);
            if(subject == null) return notFound();
            ExternalExamType ex_type = get_ex_type(type);
            return ok(Json.toJson(student.getExRecord().getExam(ex_type, subject)));
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

        class TMPSubjectClass {
            public final String subject;
            public final String teacher;
            public final int grade;
            public final int year;
            public final int semester;

            private TMPSubjectClass(SubjectClass t){
                subject = t.getSubject().getName();
                teacher = t.getTeacher().get_name();
                grade = t.getGrade();
                year = t.getSemester().getYear();
                semester = t.getSemester().getSemester();
            }
        }

        ArrayList<SubjectClass> classes = teacher.getClasses();
        ArrayList<TMPSubjectClass> json = new ArrayList<>();
        for(SubjectClass t: classes){
            json.add(new TMPSubjectClass(t));
        }

        return ok(Json.toJson(json));
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
            SubjectClass subjectClass = get_subject_class(teacher, name, grade, year, semester);
            if(subjectClass == null) return notFound();
            return ok(Json.toJson(subjectClass.getTests()));
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
            SubjectClass subjectClass = get_subject_class(teacher, name, grade, year, semester);
            if(subjectClass == null) return notFound();
            SchoolTest test = get_school_test(subjectClass, year, semester, term);
            if(test == null) return notFound();
            ArrayList<TestResult> testResults = new ArrayList<>(test.getResult().values());

            class TMPResult {
                public final String student;
                public final int score;
                public final double d_value;
                public final int rank;

                private TMPResult(TestResult t, double d_value, int rank){
                    student = t.getStudent().get_name();
                    score = t.getScore();
                    this.d_value = d_value;
                    this.rank = rank;
                }
            }

            ArrayList<TMPResult> tmp = new ArrayList<>();
            for(TestResult t : testResults){
                tmp.add(new TMPResult(t, get_d_value(t.getStudent(), test.getExam().getTime(), t.getSubject().getSubject()), 0));
            }

            return ok(Json.toJson(tmp));
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * 教師が試験実施日を確定したらその情報を持つSchoolTestを作っておく
     * @return 作成したSchoolTestが追加された、subjectClassのtestsリスト
     */
    public Result add_test(){
        try {
            final String account_id = get_id();
            if(account_id == null) return badRequest();
            Teacher teacher = get_teacher(account_id);
            if(teacher == null) return badRequest();
            Map<String, String[]> form = request().body().asFormUrlEncoded();
            int year = Integer.parseInt(form.get("year")[0]);
            int semester = Integer.parseInt(form.get("semester")[0]);
            int term = Integer.parseInt(form.get("term")[0]);
            String name = form.get("name")[0];
            int grade = Integer.parseInt(form.get("grade")[0]);
            SubjectClass subjectClass = get_subject_class(teacher, name, grade, year, semester);
            if(subjectClass == null) return notFound();
            //管理者が作ったSchoolExamリストの中から時期の一致するものを特定
            SchoolExam exam = get_school_exam(year, semester, term);
            if(exam == null) return notFound();
            final int month = Integer.parseInt(form.get("month")[0]);
            final int day = Integer.parseInt(form.get("day")[0]);
            final int division = Integer.parseInt(form.get("division")[0]);
            SchoolTime schoolTime = new SchoolTime(year, month, day, division);
            //目的のインスタンス作成 (作成後はSchoolExamやSubjectClassから参照できる)
            new SchoolTest(exam, schoolTime, subjectClass);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * 一人の生徒の試験結果を登録する
     * @return TestResultのリスト
     */
    public Result add_result(){
        try {
            final String account_id = get_id();
            if(account_id == null) return badRequest();
            Teacher teacher = get_teacher(account_id);
            if(teacher == null) return badRequest();
            Map<String, String[]> form = request().body().asFormUrlEncoded();
            int year = Integer.parseInt(form.get("year")[0]);
            int semester = Integer.parseInt(form.get("semester")[0]);
            int term = Integer.parseInt(form.get("term")[0]);
            String name = form.get("name")[0];
            int grade = Integer.parseInt(form.get("grade")[0]);
            SubjectClass subjectClass = get_subject_class(teacher, name, grade, year, semester);
            if(subjectClass == null) return notFound();
            SchoolTest test = get_school_test(subjectClass, year, semester, term);
            if(test == null) return notFound();
            //とりあえず点数と生徒IDの受け取りのみ実装
            int score = Integer.parseInt(form.get("score")[0]);
            String student_id = form.get("id")[0];
            Student student = get_student(student_id);
            if(student == null) return notFound();
            TestResult result = new TestResult(score, student, subjectClass, test.getExam().getTime());
            test.addResult(result);

            ArrayList<TestResult> testResults = new ArrayList<>(test.getResult().values());
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }


    /**
     * 模試一覧を得る
     * @return ExternalExamのリスト
     */
    public Result external_exam_list(){
        final String account_id = get_id();
        if(account_id == null) return badRequest();
        Teacher teacher = get_teacher(account_id);
        if(teacher == null) return badRequest();
        return ok(Json.toJson(ex_exams));
    }


    /**
     * 一人の生徒の模試結果を登録する
     * @return 0
     */
    public Result add_ex_result(){
        try {


            final String account_id = get_id();
            if(account_id == null) return badRequest();
            Teacher teacher = get_teacher(account_id);
            if(teacher == null) return badRequest();
            //とりあえず科目名と点数と生徒IDの受け取りで実装
            Map<String, String[]> form = request().body().asFormUrlEncoded();
            int year = Integer.parseInt(form.get("year")[0]);
            int month = Integer.parseInt(form.get("month")[0]);
            int day = Integer.parseInt(form.get("day")[0]);
            String type = form.get("type")[0];
            String name = form.get("name")[0];
            int score = Integer.parseInt(form.get("score")[0]);
            String student_id = form.get("id")[0];
            double d_value = Double.parseDouble(form.get("d_value")[0]);
            int rank = Integer.parseInt(form.get("rank")[0]);
            Subject subject = get_subject(name);
            if(subject == null) return notFound();
            Student student = get_student(student_id);
            if(student == null) return notFound();
            ExternalTest test = get_external_test(year, month, day, type, name);
            if(test == null) return notFound();
            ExternalTestResult result = new ExternalTestResult(score, student, subject, d_value, rank);
            test.addResult(result);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
    }
}
