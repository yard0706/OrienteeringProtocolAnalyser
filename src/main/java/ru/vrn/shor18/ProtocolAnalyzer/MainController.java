package ru.vrn.shor18.ProtocolAnalyzer;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@RestController
public class MainController {

    @GetMapping("/stat/title")
    @CrossOrigin
    public String eventTitle(@RequestParam String uri) {
        String titleResult = "no title for "+uri;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .build();

            HttpResponse<Stream<String>> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofLines());

            boolean isBuild = false;
            StringBuffer sbTitle = new StringBuffer();
            for(String line: response.body().toList()) {
                if (line.contains("<h1>")) isBuild = true;
                if (line.contains("</h1>")) break;
                if (isBuild) {
                    sbTitle.append(line);
                }
            }

            titleResult = sbTitle.toString().replaceAll("<.*?>", " ").replaceAll("ПРОТОКОЛ РЕЗУЛЬТАТОВ", "").trim();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return titleResult;
    }

    @GetMapping("/stat")
    @CrossOrigin
    public List<Sportsman> eventStatistic(@RequestParam String uri) {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .build();

            HttpResponse<Stream<String>> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofLines());

            List<Sportsman> sportsmens = extractSportsmensStatistics(response);

            return sportsmens;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static List<Sportsman> extractSportsmensStatistics(HttpResponse<Stream<String>> response) {
        List<Sportsman> sportsmens = new ArrayList<>();

        String group = "EMPTY";
        List<String> responseBodyLines = response.body().toList();
        int currentLine = -1;
        for(String line: responseBodyLines) {
            currentLine++;
            Matcher matcher = Pattern.compile("<h2>(.*)</h2>").matcher(line);

            if(matcher.matches()) group = matcher.group(1);

            if(Pattern.compile("\\d+\\s+[А-Я][а-яё]+\\s[А-Я][а-яё]+").matcher(line).find()) {
                line = line.replaceAll("</?b>", "");

                Sportsman sportsman = new Sportsman();
                sportsman.setGroup(group);

                Matcher matcherName = Pattern.compile("^\\s*\\d+\\s+([А-Я][а-яё]+\\s[А-Я][а-яё]+)\\s{2,}").matcher(line);
                if (matcherName.find())
                    sportsman.setName(matcherName.group(1).trim());

//                String descriptionAndPlaceRegex = "^\\s*\\d+\\s+[А-Я][а-яё]+\\s[А-Я][а-яё]+\\s{2,}(.*\\s{2,}\\+\\d\\d:\\d\\d)\\s{2,}\\=?\\s+(\\d+)\\s{2,}(\\d\\d.*)";
                String descriptionAndPlaceRegex = "^\\s*\\d+\\s+[А-Я][а-яё]+\\s[А-Я][а-яё]+\\s{2,}(.*\\s{2,}\\+?\\d?\\d?:?\\d?\\d?)\\s{2,}\\=?\\s+(\\d+)\\s{2,}(\\d\\d.*)";
                Matcher matcherPlaceAndDescription = Pattern.compile(descriptionAndPlaceRegex).matcher(line);
                if (matcherPlaceAndDescription.find())
                {
                    sportsman.setDescription(matcherPlaceAndDescription.group(1).trim());
                    sportsman.setResultPlace(matcherPlaceAndDescription.group(2).trim());
                    List<ControlPoint> controlPoints = new ArrayList<>();
                    List<ControlPoint> cumulativeControlPoints = new ArrayList<>();
                        fillControlPointsList(matcherPlaceAndDescription.group(3), controlPoints);
                        String nextLineWithCumulativeResults = responseBodyLines.get(currentLine+1);
                        Matcher isNextLineWithCumulativeResultsMatcher = Pattern.compile("^\\s*\\d+\\s+([А-Я][а-яё]+\\s[А-Я][а-яё]+)\\s{2,}")
                                .matcher(nextLineWithCumulativeResults);
                        if (!isNextLineWithCumulativeResultsMatcher.find())
                            fillControlPointsList(nextLineWithCumulativeResults.trim(), cumulativeControlPoints);
                    sportsman.setControlPoints(controlPoints);
                    sportsman.setCumulativeControlPoints(cumulativeControlPoints);
                } else continue;


                sportsmens.add(sportsman);
            }
        }
        return sportsmens;
    }

    private static void fillControlPointsList(String controlPointsLine, List<ControlPoint> controlPoints) {
        int pointNum=1;
        for(String kp: controlPointsLine
                .replaceAll("(\\))\\s+(\\d)", "$1|$2").split("\\|")) {
            String controlPointRegex = "(\\d\\d\\:\\d\\d\\:\\d\\d)\\((\\s+?\\d+)\\)\\((\\s+?\\d+)\\)";
            Matcher matcherControlPoint = Pattern.compile(controlPointRegex).matcher(kp);
            if (matcherControlPoint.find()) {
                ControlPoint controlPoint = new ControlPoint();
                controlPoint.setDuration(matcherControlPoint.group(1).trim());
                controlPoint.setPointNumber(pointNum+" - №"+matcherControlPoint.group(2).trim());
                controlPoint.setPlace(matcherControlPoint.group(3).trim());
                controlPoints.add(controlPoint);
            } else {
                controlPointRegex = "(\\d\\d\\:\\d\\d\\:\\d\\d)\\((\\s+?\\d+)\\)";
                matcherControlPoint = Pattern.compile(controlPointRegex).matcher(kp);
                if (matcherControlPoint.find()) {
                    ControlPoint controlPoint = new ControlPoint();
                    controlPoint.setDuration(matcherControlPoint.group(1).trim());
                    controlPoint.setPointNumber(pointNum+" - №"+matcherControlPoint.group(2).trim());
                    controlPoint.setPlace("0");
                    controlPoints.add(controlPoint);
                }
            }
            pointNum++;
        }
    }


}


