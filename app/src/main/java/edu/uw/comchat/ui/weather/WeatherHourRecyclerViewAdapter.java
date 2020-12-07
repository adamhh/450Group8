package edu.uw.comchat.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    public class WeatherHourViewHolder extends RecyclerView.ViewHolder {
        private FragmentWeatherHourCardBinding mBinding;
        private WeatherReport mReport;

        public WeatherHourViewHolder(@NonNull View view) {
            super(view);
            mBinding = FragmentWeatherHourCardBinding.bind(view);
        }

        /**
         * Sets the hourly weather report.
         * @param report the report of the view holder.
         */
        void setReport(final WeatherReport report) {
            mReport = report;
            mBinding.textWeatherHourTime.setText(report.getTime());
            mBinding.imageWeatherHour.setImageResource(R.drawable.broken_clouds);
            mBinding.textWeatherHourTemp.setText(report.getTemp());
        }
    }
}
