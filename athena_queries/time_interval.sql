SELECT weather_description.interval, weather_description.city
  , city_attributes.country, city_attributes.latitude, city_attributes.longitude
  , weather_description.description, weather_description.total, weather_description.percent
  , humidity.min humidity_min, humidity.max humidity_max, humidity.average humidity_average
  , pressure.min pressure_min, pressure.max pressure_max, pressure.average pressure_average
  , temperature.min temperature_min, temperature.max temperature_max, temperature.average temperature_average
  , wind_direction.min wind_direction_min, wind_direction.max wind_direction_max, wind_direction.average wind_direction_average
  , wind_speed.min wind_speed_min, wind_speed.max wind_speed_max, wind_speed.average wind_speed_average
FROM weather_description_time_interval weather_description
JOIN humidity_time_interval humidity 
  ON weather_description.interval = humidity.interval
  AND weather_description.city = humidity.city
JOIN pressure_time_interval pressure
  ON weather_description.interval = pressure.interval
  AND weather_description.city = pressure.city
JOIN temperature_time_interval temperature 
  ON weather_description.interval = temperature.interval
  AND weather_description.city = temperature.city
JOIN wind_direction_time_interval wind_direction 
  ON weather_description.interval = wind_direction.interval
  AND weather_description.city = wind_direction.city
JOIN wind_speed_time_interval wind_speed 
  ON weather_description.interval = wind_speed.interval
  AND weather_description.city = wind_speed.city
JOIN city_attributes ON weather_description.city = city_attributes.city
WHERE weather_description.city = 'Los Angeles'
ORDER BY weather_description.interval