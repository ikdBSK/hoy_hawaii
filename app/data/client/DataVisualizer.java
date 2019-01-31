package data.client;

import play.libs.Json;
import play.mvc.Result;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import static play.mvc.Results.ok;

public class DataVisualizer {
    // クライアント側に、可視化されたデータを送信するための処理クラス

    // BufferedImageをJSONに変換できるデータ構造に変換
    private static String img2Base64Png(final RenderedImage img) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (IOException e) {
            System.out.println("IO ERROR");
            return "";
        }
    }

    private final static int stick_width = 10;
    private final static int stick_height = 100;
    private final static int stick_padding = 8;

    public static Result subject_history(ArrayList<Double> d_value_list, ArrayList<Double> rate_list, ArrayList<Integer> rank_list) throws IllegalArgumentException{
        if(!(d_value_list.size() == rate_list.size() && rate_list.size() == rank_list.size()))
            throw new IllegalArgumentException();

//        Color color = new Color()

        return ok(Json.toJson(""));
    }

    private static Color[] generate_colors(int count){
        ArrayList<Color> colors = new ArrayList<>();

//        int

        return colors.toArray(new Color[0]);
    }
}
