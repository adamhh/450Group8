package edu.uw.comchat.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalTime;
import java.util.List;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentWeatherHourCardBinding;

/**
 * A recycler view adapter to be used for the weather reports for the hour.
 *
 * @author Jerry Springer
 * @version 6 December 2020
 */
public class WeatherHourRecyclerViewAdapter extends
        RecyclerView.Adapter<WeatherHourRecyclerViewAdapter.WeatherHourViewHolder>{

    /** The list of weather reports **/
    private final List<WeatherReport> mReports;

    public WeatherHourRecyclerViewAdapter(List<WeatherReport> reports) {
        mReports = reports;
    }

    @NonNull
    @Override
    public WeatherHourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherHourViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_weather_hour_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHourViewHolder holder, int position) {
        holder.setReport(mReports.get(position));
    }

    @Override
    public int getItemCount() {
        return mReports.size();
    }

    /**
     * Objects from this class represent a single weather column view for the hour.
     */
    public static class WeatherHourViewHolder extends RecyclerView.ViewHolder {
        private final FragmentWeatherHourCardBinding mBinding;

        public WeatherHourViewHolder(@NonNull View view) {
            super(view);
            mBinding = FragmentWeatherHourCardBinding.bind(view);
        }

        /**
         * Sets the hourly weather report.
         * @param report the report of the view holder.
         */
        void setReport(final WeatherReport report) {
            mBinding.textWeatherHourTime.setText(report.getTime());
            mBinding.textWeatherHourTemp.setText(report.getTemp());

            String[] split = report.getTime().split(" ");
            int hour = Integer.parseInt(split[0]);
            if (split[1].equals("pm"))
                hour += 12;

            hour %= 24;

            int id = report.getId();
            if (id >= 200 && id <= 232) { // ID for thunderstorm
                mBinding.imageWeatherHour.setImageResource(R.drawable.ic_home_thunder_storm);
            } else if (id >= 300 && id <= 321) { // ID for drizzle
                mBinding.imageWeatherHour.setImageResource(R.drawable.ic_home_drizzle);
            } else if (id >= 500 && id <= 531) { // ID for rain
                mBinding.imageWeatherHour.setImageResource(R.drawable.ic_home_rain);
            } else if (id >= 600 && id <= 622) { // ID for snow
                mBinding.imageWeatherHour.setImageResource(R.drawable.ic_home_snow);
            } else if (id >= 700 && id <= 781) { // ID for atmosphere
                mBinding.imageWeatherHour.setImageResource(R.drawable.ic_home_fog);
            } else if (id == 800) {
                LocalTime time = LocalTime.of(hour, 0);
                if (time.isAfter(LocalTime.parse(report.getSunrise())) && time.isBefore(LocalTime.parse(report.getSunset()))) {
                    mBinding.imageWeatherHour.setImageResource(R.drawable.ic_home_clear_sky_day);
                } else {
                    mBinding.imageWeatherHour.setImageResource(R.drawable.ic_home_clear_sky_night);
                }
            } else if (id >= 801 && id <= 804) { // ID for cloud
                mBinding.imageWeatherHour.setImageResource(R.drawable.ic_home_cloudy);
            }
        }
    }
}
