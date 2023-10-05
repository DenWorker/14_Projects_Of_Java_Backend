package com.example.SensorsRegistrationsRestAPI;

import com.example.SensorsRegistrationsRestAPI.dto.MeasurementDTO;
import com.example.SensorsRegistrationsRestAPI.models.Measurement;
import com.example.SensorsRegistrationsRestAPI.models.Sensor;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.lines.SeriesLines;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class SensorsRegistrationsRestApiApplication {

    private static final RestTemplate response = new RestTemplate();


    public static void main(String[] args) throws IOException {
        SpringApplication.run(SensorsRegistrationsRestApiApplication.class, args);
        System.out.println("Hello world!");

        //sensorsPost();
        List<Sensor> sensorList = getSensorsFromServerNotDTO();
        System.out.println(sensorList);

        //measurementPost(getSensorsFromServerNotDTO());
        List<Measurement> measurementList = getMeasurementsFromServerNotDTO();
        System.out.println(measurementList);

        int id = 9;
        createGraphicOfSensorOnID(getMeasurementsFromServerNotDTO(), id);
    }

    private static void sensorsPost() {
        List<Sensor> sensorList = new ArrayList<>();
        sensorList.add(new Sensor("DHT11"));
        sensorList.add(new Sensor("DHT22"));
        sensorList.add(new Sensor("DS18B20"));
        sensorList.add(new Sensor("BME280"));
        sensorList.add(new Sensor("AM2302"));
        sensorList.add(new Sensor("HTU21D"));
        sensorList.add(new Sensor("SHT31"));
        sensorList.add(new Sensor("BMP180"));
        sensorList.add(new Sensor("DHT12"));
        sensorList.add(new Sensor("DS18S20"));
        sensorList.add(new Sensor("BME680"));
        sensorList.add(new Sensor("AM2315"));
        sensorList.add(new Sensor("HTU20D"));
        sensorList.add(new Sensor("SHT35"));
        sensorList.add(new Sensor("BMP280"));
        sensorList.add(new Sensor("DHT11S"));
        sensorList.add(new Sensor("DS18S22"));
        sensorList.add(new Sensor("DHT23"));
        sensorList.add(new Sensor("BME770"));
        sensorList.add(new Sensor("DHT33"));

        for (Sensor sensor : sensorList) {
            response.postForObject(
                    "http://localhost:8080/sensors/registration",
                    new HttpEntity<>(sensor),
                    String.class);
        }
    }

    private static List<Sensor> getSensorsFromServerNotDTO() {
        ResponseEntity<List<Sensor>> responseEntity = response.exchange(
                "http://localhost:8080/sensors/notDTO",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return responseEntity.getBody();
    }

    private static void measurementPost(List<Sensor> sensorList) {
        for (int i = 0; i < 1000; i++) {
            MeasurementDTO measurementDTO = new MeasurementDTO(
                    ThreadLocalRandom.current().nextDouble(-100.00, 100.00),
                    ThreadLocalRandom.current().nextBoolean(),
                    sensorList.get(ThreadLocalRandom.current().nextInt(0, sensorList.size() - 1)));
            response.postForObject(
                    "http://localhost:8080/measurements/add",
                    new HttpEntity<>(measurementDTO),
                    String.class);
        }
    }

    private static List<Measurement> getMeasurementsFromServerNotDTO() {
        ResponseEntity<List<Measurement>> responseEntity = response.exchange(
                "http://localhost:8080/measurements/notDTO",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return responseEntity.getBody();
    }

    private static void createGraphicOfSensorOnID(List<Measurement> measurementList, int id) throws IOException {
        List<Long> xData = new ArrayList<>();
        List<Double> yData = new ArrayList<>();

        measurementList.stream().filter(t -> t.getSensor().getId() == id).forEach(t -> {
            xData.add(t.getMeasurementTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            yData.add(t.getValue());
        });

        XYChart chart = new XYChartBuilder().width(2000).height(600).title("График зависимости температуры от времени датчика с id " + id).xAxisTitle("X (milliSec)").yAxisTitle("Y (t)").build();
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        chart.addSeries("Данные", xData, yData).setLineStyle(SeriesLines.DOT_DOT);


        BitmapEncoder.saveBitmap(chart, "C:\\Users\\ivano\\desktop\\chart.png", BitmapEncoder.BitmapFormat.PNG);
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
