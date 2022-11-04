SELECT weather_description.month, weather_description.city
  , city_attributes.country, city_attributes.latitude, city_attributes.longitude
  , weather_description.description, weather_description.total, weather_description.percent
  , humidity.min humidity_min, humidity.max humidity_max, humidity.average humidity_average
  , pressure.min pressure_min, pressure.max pressure_max, pressure.average pressure_average
  , temperature.min temperature_min, temperature.max temperature_max, temperature.average temperature_average
  , wind_direction.min wind_direction_min, wind_direction.max wind_direction_max, wind_direction.average wind_direction_average
  , wind_speed.min wind_speed_min, wind_speed.max wind_speed_max, wind_speed.average wind_speed_average
FROM weather_description_monthly weather_description
JOIN humidity_monthly humidity 
  ON weather_description.month = humidity.month
  AND weather_description.city = humidity.city
JOIN pressure_monthly pressure
  ON weather_description.month = pressure.month
  AND weather_description.city = pressure.city
JOIN temperature_monthly temperature 
  ON weather_description.month = temperature.month
  AND weather_description.city = temperature.city
JOIN wind_direction_monthly wind_direction 
  ON weather_description.month = wind_direction.month
  AND weather_description.city = wind_direction.city
JOIN wind_speed_monthly wind_speed 
  ON weather_description.month = wind_speed.month
  AND weather_description.city = wind_speed.city
JOIN city_attributes ON weather_description.city = city_attributes.city
WHERE weather_description.city = 'Los Angeles'
ORDER BY weather_description.month