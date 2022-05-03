package model;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/areaCheck")
public class AreaCheckServlet extends HttpServlet {
    private String strip(String s) {
        if (s == null) return s;
        return s.substring(0, Math.min(s.length(), 13));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long startTime = System.currentTimeMillis();

        List<Double> xValues, yValues, rValues;
        try {
            xValues = Arrays.stream(req.getParameterValues("x"))
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());
            yValues = Arrays.stream(req.getParameterValues("x"))
                    .map(this::strip)
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());
            rValues = Arrays.stream(req.getParameterValues("r"))
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            resp.setStatus(404);
            return;
        }

        boolean xError = xValues.size() != 1;
        boolean yError = yValues.size() != 1;
        boolean rError = rValues.size() != 1;

        if (xError || yError || rError) {
            resp.setStatus(404);
            return;
        }

        HttpSession session = req.getSession();
        ArrayList<Response> responses = (ArrayList<Response>) session.getAttribute("responses");

        if (responses == null) {
            responses = new ArrayList<>();
        }

        double x = xValues.get(0);
        double y = yValues.get(0);
        Double R = rValues.get(0);
        //треугольник
        boolean hit = (x >= 0 && y >= 0 && y <= -x + R / 2) ||
                //прямоугольник
                (x <= 0 && y >= 0 && x >= -R && y <= R) ||
                //четверть круга
                (x <= 0 && y <= 0 && x * x + y * y <= (R / 2) * (R / 2));

        Dot dot = new Dot(x, y, (double) R, hit, "fuf");

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        String currentTime = hours + ":" + minutes + ":" + seconds;
        long workingTime = System.currentTimeMillis() - startTime;

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Response response = new Response(dot, workingTime, currentTime);
        responses.add(response);
        session.setAttribute("responses", responses);

        Gson gson = new Gson();
        String JSONResponse = gson.toJson(response);
        resp.getWriter().print(JSONResponse);
    }
}
