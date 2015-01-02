package cz.hcs.app.simpledrawforkids;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.UUID;

import static cz.hcs.app.simpledrawforkids.R.id.stroke_btn;
import static cz.hcs.app.simpledrawforkids.R.id.stroke_layout;

public class MainActivity extends Activity implements OnClickListener {
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, openBtn, strokeBtn;
    private Layout strokeLayout;
    private float smallBrush, mediumBrush, largeBrush;
    private DrawingView drawView;
    private String eraserColor = "white";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);
        drawView = (DrawingView) findViewById(R.id.drawing);
        drawView.setSaveEnabled(true);
        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.border_paint_selected));
        drawView.setBrushSize(mediumBrush);
        newBtn = (ImageButton) findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);
        openBtn = (ImageButton) findViewById(R.id.open_btn);
        openBtn.setOnClickListener(this);
        saveBtn = (ImageButton) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);
        strokeBtn = (ImageButton) findViewById(stroke_btn);
        strokeBtn.setOnClickListener(this);
        drawBtn = (ImageButton) findViewById(R.id.draw_btn);
        eraseBtn = (ImageButton) findViewById(R.id.erase_btn);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.stroke_btn) {
            final Dialog strokeDialog = new Dialog(this);
            strokeDialog.setTitle(R.string.stroke_size);
            strokeDialog.setContentView(R.layout.stroke_chooser);
            ImageButton smallBtn = (ImageButton) strokeDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    strokeBtn.setBackgroundResource(R.drawable.small);
                    strokeDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton) strokeDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    strokeBtn.setBackgroundResource(R.drawable.medium);
                    strokeDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton) strokeDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    strokeBtn.setBackgroundResource(R.drawable.large);
                    strokeDialog.dismiss();
                }
            });
            strokeDialog.show();
        } else if (view.getId() == R.id.new_btn) {
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle(R.string.new_title);
            newDialog.setMessage(R.string.new_warning);
            newDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            newDialog.show();
        } else if (view.getId() == R.id.save_btn) {
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle(R.string.save_title);
            saveDialog.setMessage(R.string.save_warning);
            saveDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    drawView.setDrawingCacheEnabled(true);
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString() + ".png", "drawing");
                    if (imgSaved != null) {
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                R.string.save_succes, Toast.LENGTH_SHORT);
                        savedToast.show();
                    } else {
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                R.string.save_failed, Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
    }

    public void strokeClicked(View view) {
        final Dialog strokeDialog = new Dialog(this);
        strokeDialog.setTitle(R.string.stroke_size);
        strokeDialog.setContentView(R.layout.stroke_chooser);
        ImageButton smallBtn = (ImageButton) strokeDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setBrushSize(smallBrush);
                strokeBtn.setBackgroundResource(R.drawable.small);
                strokeDialog.dismiss();
            }
        });
        ImageButton mediumBtn = (ImageButton) strokeDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setBrushSize(mediumBrush);
                strokeBtn.setBackgroundResource(R.drawable.medium);
                strokeDialog.dismiss();
            }
        });
        ImageButton largeBtn = (ImageButton) strokeDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setBrushSize(largeBrush);
                strokeBtn.setBackgroundResource(R.drawable.large);
                strokeDialog.dismiss();
            }
        });
        strokeDialog.show();
    }

    public void brushClicked(View view) {
        if (drawView.isErase()){
        drawView.setErase(false);
        }
        changeActiveButton(drawView.isErase());
    }

    public void rubberClicked(View view) {
        drawView.setErase(true);
        changeActiveButton(drawView.isErase());
    }

    public void paintClicked(View view) {
        if (view != currPaint) {
            drawView.setErase(false);
            changeActiveButton(drawView.isErase());
            ImageButton imgView = (ImageButton) view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.border_paint_selected));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.border_paint_default));
            currPaint = (ImageButton) view;
        }
    }

    public void changeActiveButton(boolean isErase) {
        if (isErase) {
            drawBtn.setBackgroundResource(R.drawable.brush_default);
            eraseBtn.setBackgroundResource(R.drawable.rubber_selected);
        } else {
            drawBtn.setBackgroundResource(R.drawable.brush_selected);
            eraseBtn.setBackgroundResource(R.drawable.rubber_default);
        }
    }

}
