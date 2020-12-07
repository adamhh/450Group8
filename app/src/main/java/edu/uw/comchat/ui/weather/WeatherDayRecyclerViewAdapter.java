package edu.uw.comchat.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentWeatherDayCardBinding;

/**
 * A recycler view adapter to be used for the weather reports for the day.
 *
 * @author Jerry Springer
 * @version 6 December 2020
 */
public class WeatherDayRecyclerViewAdapter extends
        RecyclerView.Adapter<WeatherDayRecyclerViewAdapter.WeatherDayViewHolder>{

    /** The list of weather reports **/
    private final List<WeatherReport> mReports;

    public WeatherDayRecyclerViewAdapter(List<WeatherReport> reports) {
        mReports = reports;
    }

    @NonNull
    @Override
    public WeatherDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherDayViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_weather_day_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDayViewHolder holder, int position) {
        holder.setReport(mReports.get(position));
    }

    @Override
    public int getItemCount() {
        return mReports.size();
    }

    /**
     * Objects from this class represent a single weather row view for the day.
     */
    public class WeatherDayViewHolder extends RecyclerView.ViewHolder {
        private FragmentWeatherDayCardBinding mBinding;
        private WeatherReport mReport;

        public WeatherDayViewHolder(@NonNull View view) {
            super(view);
            mBinding = FragmentWeatherDayCardBinding.bind(view);
        }

        /**
         * Sets the daily weather report.
         * @param report the report of the view holder.
         */
        void setReport(final WeatherReport report) {
            mReport = report;
            mBinding.textWeatherDay.setText(report.getTime());
            mBinding.imageWeatherDay.setImageResource(R.drawable.broken_clouds);
            mBinding.textWeatherDayTemp.setText(report.getTemp());
        }
    }
}
