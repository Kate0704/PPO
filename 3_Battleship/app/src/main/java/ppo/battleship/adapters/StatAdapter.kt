package ppo.battleship.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ppo.battleship.R
import java.util.*
import ppo.battleship.activities.StatisticsActivity.StatItem

class StatAdapter(list: ArrayList<StatItem>) :
    RecyclerView.Adapter<StatAdapter.StatViewHolder>() {
    private val gameStatistics: ArrayList<StatItem> = list

    class StatViewHolder(private val statView: View) : RecyclerView.ViewHolder(statView) {
        val serverName: TextView = statView.findViewById(R.id.player1_name)
        val clientName: TextView = statView.findViewById(R.id.player2_name)
        val serverScore: TextView = statView.findViewById(R.id.score_player1)
        val clientScore: TextView = statView.findViewById(R.id.score_player2)
    }

    override fun onCreateViewHolder(parent: ViewGroup,type: Int): StatViewHolder =
        StatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.stat_item, parent, false))

    override fun onBindViewHolder(holder: StatViewHolder,position: Int) {
        val statItem: StatItem = gameStatistics[position]
        holder.serverName.text = statItem.getPlayer1()
        holder.clientName.text = statItem.getPlayer2()
        holder.serverScore.text = statItem.getScore1().toString()
        holder.clientScore.text = statItem.getScore2().toString()
    }

    override fun getItemCount(): Int = gameStatistics.size
}