package com.breckworld.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.activity.VisitActivity
import com.breckworld.model.home.Button
import com.breckworld.model.home.Slide
import com.breckworld.model.home.SlideX
import com.breckworld.util.AppUtil
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso


class HomeAdapter(
    private val context: Context,
    private var buttonList: List<Button>,
    private var slides: List<Slide>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private var slideList: List<Slide>? = null
    private var buttonClicked: Boolean = false
    private var shouldFadeIn: Boolean = false
    private var layoutBottomLeft: RelativeLayout? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home, parent, false)
        return ViewHolderForNewUser(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderType = (holder as ViewHolderForNewUser)

        layoutBottomLeft = holderType.layoutBottomLeft
        if (buttonClicked) {

            /*Glide.with(context).load(slideList?.get(position)?.images?.logo)
                .into(holderType.imageViewLogo)*/
            Picasso.get().load(slideList?.get(position)?.images?.logo)
                .into(holderType.imageViewLogo)
            Glide.with(context).load(slideList?.get(position)?.images?.slide_full)
                .into(holderType.imageViewImage)
            holderType.textViewCompanyTitle.text = slideList?.get(position)?.labels?.header
            holderType.textViewCompanySubTitle.text = slideList?.get(position)?.labels?.subheader
            holderType.txtTitle.text = slideList?.get(position)?.labels?.title
            holderType.txtDesc.visibility = View.INVISIBLE
            holderType.txtDesc.maxLines = 3
            holderType.txtDesc.text = slideList?.get(position)?.descriptions?.long

            if (shouldFadeIn) {
                holderType.layoutBottomLeft.alpha = 0.5f
            } else {
                holderType.layoutBottomLeft.alpha = 1f
            }

            val upperString: String = slideList?.get(position)?.labels?.link?.substring(0, 1)?.toUpperCase() + slideList?.get(position)?.labels?.link?.substring(1)?.toLowerCase()
            holderType.textViewVisitThisPage.text = HtmlCompat.fromHtml(
                "<u> $upperString</u>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            var lineCount = 0
            holderType.txtDesc.post(Runnable {
                lineCount = holderType.txtDesc.getLineCount()

                if (lineCount > 3) {
                    var trimmedText = ""
                    holderType.txtDesc.post(Runnable {
                        trimmedText = AppUtil.getVisibleText(holderType.txtDesc).toString()
                        //trimmedText.dropLast(13)
                        holderType.txtDesc.text = HtmlCompat.fromHtml(
                            AppUtil.removeChars(trimmedText, 10)?.substringBeforeLast(" ") + "<b> ... more</b>",
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                    })
                    holderType.txtDesc.visibility = View.VISIBLE

                } else {
                    holderType.txtDesc.maxLines = 10
                    holderType.txtDesc.text = slideList?.get(position)?.descriptions?.long
                    holderType.txtDesc.visibility = View.VISIBLE
                }
            })

            if (slideList?.get(position)?.ids?.linked_mds != null) {
                holderType.layoutVisitThisPage.visibility = View.VISIBLE
            } else {
                holderType.layoutVisitThisPage.visibility = View.INVISIBLE
            }

            holderType.imageViewForHeaderAlpha.setBackgroundColor(
                Color.parseColor(
                    slideList?.get(
                        position
                    )?.colors?.header_bg
                )
            )
            holderType.imageViewForHeaderAlpha.alpha =
                slideList?.get(position)?.colors?.header_opacity!!.toFloat()
            holderType.textViewCompanyTitle.setTextColor(Color.parseColor(slideList?.get(position)?.colors?.header_text))
            holderType.textViewCompanySubTitle.setTextColor(Color.parseColor(slideList?.get(position)?.colors?.header_text))

            holderType.txtDesc.setOnClickListener {
                var moreWord =
                    holderType.txtDesc.text.substring(holderType.txtDesc.text.lastIndexOf(" ") + 1)
                if (moreWord.contains("more")) {
                    holderType.txtDesc.maxLines = 10
                    holderType.txtDesc.text = HtmlCompat.fromHtml(
                        slideList?.get(position)?.descriptions?.long + "   <b>less</b>",
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                } else {
                    holderType.txtDesc.post(Runnable {
                        lineCount = holderType.txtDesc.getLineCount()

                        if (lineCount > 3) {
                            holderType.txtDesc.maxLines = 3
                            var trimmedText = ""
                            holderType.txtDesc.post(Runnable {
                                trimmedText = AppUtil.getVisibleText(holderType.txtDesc).toString()
                                holderType.txtDesc.text = HtmlCompat.fromHtml(
                                    AppUtil.removeChars(trimmedText, 10)?.substringBeforeLast(" ") + "<b> ... more</b>",
                                    HtmlCompat.FROM_HTML_MODE_LEGACY
                                )
                            })
                        } else {
                            holderType.txtDesc.maxLines = 10
                            holderType.txtDesc.text = slideList?.get(position)?.descriptions?.long
                        }
                    })
                }
            }

            holderType.layoutVisitThisPage.setOnClickListener {
                context?.startActivity(
                    Intent(context, VisitActivity::class.java)
                        .putExtra("cameFrom", "Home")
                        .putExtra("linkedMdsId", slideList!!.get(position).ids.linked_mds)
                )
            }

        } else {

            Picasso.get().load(slides.get(position).images.logo)
                .into(holderType.imageViewLogo)
            Glide.with(context).load(slides.get(position).images.slide_full)
                .into(holderType.imageViewImage)
            holderType.textViewCompanyTitle.text = slides.get(position).labels.header
            holderType.textViewCompanySubTitle.text = slides.get(position).labels.subheader
            holderType.txtTitle.text = slides.get(position).labels.title
            holderType.txtDesc.visibility = View.INVISIBLE
            holderType.txtDesc.maxLines = 3
            holderType.txtDesc.text = slides.get(position).descriptions.long

            if (shouldFadeIn) {
                holderType.layoutBottomLeft.alpha = 0.5f
            } else {
                holderType.layoutBottomLeft.alpha = 1f
            }

            val upperString: String = slides.get(position).labels.link.substring(0, 1).toUpperCase() + slides.get(position).labels.link.substring(1).toLowerCase()
            holderType.textViewVisitThisPage.text = HtmlCompat.fromHtml(
                "<u> $upperString</u>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            var lineCount = 0
            holderType.txtDesc.post(Runnable {
                lineCount = holderType.txtDesc.getLineCount()

                if (lineCount > 3) {
                    var trimmedText = ""
                    holderType.txtDesc.post(Runnable {
                        trimmedText = AppUtil.getVisibleText(holderType.txtDesc).toString()
                        holderType.txtDesc.text = HtmlCompat.fromHtml(
                            AppUtil.removeChars(trimmedText, 10)?.substringBeforeLast(" ") + "<b> ... more</b>",
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                    })
                    holderType.txtDesc.visibility = View.VISIBLE

                } else {
                    holderType.txtDesc.maxLines = 10
                    holderType.txtDesc.text = slides.get(position).descriptions.long
                    holderType.txtDesc.visibility = View.VISIBLE
                }
            })


            if (slides.get(position).ids.linked_mds != null) {
                holderType.layoutVisitThisPage.visibility = View.VISIBLE
            } else {
                holderType.layoutVisitThisPage.visibility = View.INVISIBLE
            }

            holderType.imageViewForHeaderAlpha.setBackgroundColor(
                Color.parseColor(
                    slides.get(
                        position
                    ).colors.header_bg
                )
            )
            holderType.imageViewForHeaderAlpha.alpha =
                slides.get(position).colors.header_opacity.toFloat()
            holderType.textViewCompanyTitle.setTextColor(Color.parseColor(slides.get(position).colors.header_text))
            holderType.textViewCompanySubTitle.setTextColor(Color.parseColor(slides.get(position).colors.header_text))

            holderType.txtDesc.setOnClickListener {
                var moreWord =
                    holderType.txtDesc.text.substring(holderType.txtDesc.text.lastIndexOf(" ") + 1)
                if (moreWord.contains("more")) {
                    holderType.txtDesc.maxLines = 10
                    holderType.txtDesc.text = HtmlCompat.fromHtml(
                        slides.get(position).descriptions.long + "   <b>less</b>",
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                } else {
                    holderType.txtDesc.post(Runnable {
                        lineCount = holderType.txtDesc.getLineCount()

                        if (lineCount > 3) {
                            holderType.txtDesc.maxLines = 3
                            var trimmedText = ""
                            holderType.txtDesc.post(Runnable {
                                trimmedText = AppUtil.getVisibleText(holderType.txtDesc).toString()
                                holderType.txtDesc.text = HtmlCompat.fromHtml(
                                    AppUtil.removeChars(trimmedText, 10)?.substringBeforeLast(" ") + "<b> ... more</b>",
                                    HtmlCompat.FROM_HTML_MODE_LEGACY
                                )
                            })
                        } else {
                            holderType.txtDesc.maxLines = 10
                            holderType.txtDesc.text = slides.get(position).descriptions.long
                        }
                    })
                }
            }

            holderType.layoutVisitThisPage.setOnClickListener {
                onClickListener?.onVisitThisPageClickListener(position)
            }
        }

    }

    fun updateNewData(position: Int) {
        buttonClicked = true
        slideList = buttonList.get(position).data.slides
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (buttonClicked)
            slideList!!.size
        else
            slides.size
    }

    inner class ViewHolderForNewUser(view: View) : RecyclerView.ViewHolder(view) {
        var mVideoView: VideoView = itemView.findViewById(R.id.videoView)
        var imageViewImage: ImageView = itemView.findViewById(R.id.imageViewImage)
        var imageViewLogo: ImageView = itemView.findViewById(R.id.imageViewLogo)
        var textViewCompanyTitle: TextView = itemView.findViewById(R.id.textViewCompanyTitle)
        var textViewCompanySubTitle: TextView = itemView.findViewById(R.id.textViewCompanySubTitle)
        var textViewVisitThisPage: TextView = itemView.findViewById(R.id.textViewVisitThisPage)
        var txtTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        var txtDesc: TextView = itemView.findViewById(R.id.textViewDes)
        var mProgressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        var layoutVisitThisPage: LinearLayout = itemView.findViewById(R.id.layoutVisitThisPage)
        var layoutHeader: RelativeLayout = itemView.findViewById(R.id.layoutHeader)
        var layoutBottomLeft: RelativeLayout = itemView.findViewById(R.id.layoutBottomLeft)
        var recyclerViewMDSButtons: RecyclerView =
            itemView.findViewById(R.id.recyclerViewMDSButtons)
        var imageViewForHeaderAlpha: ImageView = itemView.findViewById(R.id.imageViewForHeaderAlpha)
    }

    interface OnClickListener {
        fun onVisitThisPageClickListener(position: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun updateList(itemList: ArrayList<Slide>?) {
        buttonClicked = true
        slideList = itemList
    }

    fun setFadeIn(fadeIn: Boolean, position: Int) {
        shouldFadeIn = fadeIn
        //notifyItemChanged(position)
        if (buttonClicked && slideList!!.size > 1) {
            notifyDataSetChanged()
        } else if(slides.size > 1) {
            notifyDataSetChanged()
        }

    }

}

