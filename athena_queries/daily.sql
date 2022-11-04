SELECT weather_description.date, weather_description.city
  , city_attributes.country, city_attributes.latitude, city_attributes.longitude
  , weather_description.description, weather_description.total, weather_description.percent
  , humidity.min humidity_min, humidity.max humidity_max, humidity.average humidity_average
  , pressure.min pressure_min, pressure.max pressure_max, pressure.average pressure_average
  , temperature.min temperature_min, temperature.max temperature_max, temperature.average temperature_average
  , wind_direction.min wind_direction_min, wind_direction.max wind_direction_max, wind_direction.average wind_direction_average
  , wind_speed.min wind_speed_min, wind_speed.max wind_speed_max, wind_speed.average wind_speed_average
FROM weather_description_daily weather_description
JOIN humidity_daily humidity 
  ON weather_description.date = humidity.date
  AND weather_description.city = humidity.city
JOIN pressure_daily pressure
  ON weather_description.date = pressure.date
  AND weather_description.city = pressure.city
JOIN temperature_daily temperature 
  ON weather_description.date = temperature.date
  AND weather_description.city = temperature.city
JOIN wind_direction_daily wind_direction 
  ON weather_description.date = wind_direction.date
  AND weather_description.city = wind_direction.city
JOIN wind_speed_daily wind_speed 
  ON weather_description.date = wind_speed.date
  AND weather_description.city = wind_speed.city
JOIN city_attributes ON weather_description.city = city_attributes.city
WHERE weather_description.city = 'Los Angeles'
ORDER BY weather_description.date