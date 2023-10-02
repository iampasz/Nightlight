//package com.sarnavsky.pasz.nightlight2;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.List;
//
///**
// * Created by pasz on 14.05.2017.
// */
//
//public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{
//
//    List<AdCard> adCards;
//
//
//    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        CardView cv;
//        TextView personName;
//        TextView personAge;
//        ImageView personPhoto;
//        List<AdCard> adCards;
//
//
//
//        PersonViewHolder(View itemView, List<AdCard> adCards ) {
//            super(itemView);
//            cv = (CardView)itemView.findViewById(R.id.cv);
//            personName = (TextView)itemView.findViewById(R.id.person_name);
//            personAge = (TextView)itemView.findViewById(R.id.person_age);
//            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
//
//            this.adCards = adCards;
//
//            itemView.setOnClickListener(this);
//
//        }
//
//
//
//
//
//        @Override
//        public void onClick(View view) {
//
//
//            int link = adCards.get(getPosition()).link;
//
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse(view.getContext().getString(link)));
//            itemView.getContext().startActivity(intent);
//
//        }
//
//
//    }
//
//
//    RVAdapter(List<AdCard> adCards){
//        this.adCards = adCards;
//    }
//    @Override
//    public PersonViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
//        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item2, viewGroup, false);
//        return new PersonViewHolder(v, adCards);
//    }
//
//    @Override
//    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
//        personViewHolder.personName.setText(adCards.get(i).name);
//        personViewHolder.personAge.setText(adCards.get(i).age);
//        personViewHolder.personPhoto.setImageResource(adCards.get(i).photoId);
//    }
//
//    @Override
//    public int getItemCount() {
//        return adCards.size();
//    }
//
//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//    }
//
//
//
//
//}