package cn.finalteam.loadingviewfinal.sample.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.sample.R;
import cn.finalteam.loadingviewfinal.sample.http.model.GameInfo;
import cn.finalteam.toolsfinal.adapter.ViewHolderAdapter;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/9 下午5:21
 */
public class NewGameListAdapter extends ViewHolderAdapter<NewGameListAdapter.NewGameViewHolder, GameInfo> {

    public NewGameListAdapter(Context context, List<GameInfo> list) {
        super(context, list);
    }

    @Override
    public NewGameViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflate(R.layout.adapter_list_item, parent);
        return new NewGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewGameViewHolder holder, int position) {
        GameInfo gameInfo = getDatas().get(position);
        Picasso.with(getContext())
                .load(gameInfo.getIconUrl())
                .centerCrop()
                .placeholder(R.mipmap.ic_gf_default_photo)
                .error(R.mipmap.ic_gf_default_photo)
                .resize(300, 220)

                .config(Bitmap.Config.ARGB_8888)
                .into(holder.mIcGameIcon);
        holder.mTvGameName.setText(gameInfo.getName());
        holder.mRbGameRank.setRating(gameInfo.getTaskScore()/2.0f);
        holder.mTvGameSocre.setText(new DecimalFormat("#0.0").format(gameInfo.getTaskScore()) + "分");
        holder.mTvGamePlayerNumber.setText("热度:" + String.valueOf(gameInfo.getPlayerCount()));
        holder.mTvGameCommentNumber.setText("评论数:" + String.valueOf(gameInfo.getCommentCount()));
    }

    static class NewGameViewHolder extends ViewHolderAdapter.ViewHolder {

        @Bind(R.id.ic_game_icon)
        ImageView mIcGameIcon;
        @Bind(R.id.tv_game_name)
        TextView mTvGameName;
        @Bind(R.id.rb_game_rank)
        RatingBar mRbGameRank;
        @Bind(R.id.tv_game_socre)
        TextView mTvGameSocre;
        @Bind(R.id.tv_game_player_number)
        TextView mTvGamePlayerNumber;
        @Bind(R.id.tv_game_comment_number)
        TextView mTvGameCommentNumber;

        public NewGameViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
