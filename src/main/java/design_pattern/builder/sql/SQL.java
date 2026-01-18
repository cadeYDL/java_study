package design_pattern.builder.sql;

public class SQL {

    private SQL(){
    }

    public static UpdateStage newUpdateBuidler(){
        return new UpdateBuidler();
    }
    public static DeleteStage newDeleteBuidler(){
        return new DeleteBuidler();
    }

    public static InsertStage newInsertBuidler(){
        return new InsertBuidler();
    }

    public static SelectStage newSelectBuidler(){
        return new SelectBuidler();
    }
}
