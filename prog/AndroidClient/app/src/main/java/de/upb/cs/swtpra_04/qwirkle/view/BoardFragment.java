package de.upb.cs.swtpra_04.qwirkle.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.devs.vectorchildfinder.VectorChildFinder;

import java.util.List;

import de.upb.cs.swtpra_04.qwirkle.controller.BoardFragmentInterface;
import de.upb.cs.swtpra_04.qwirkle.R;
import de.upb.cs.swtpra_04.qwirkle.model.game.BoardTile;
import de.upb.cs.swtpra_04.qwirkle.model.game.GameData;
import de.upb.cs.swtpra_04.qwirkle.model.game.Board;
import de.upb.cs.swtpra_04.qwirkle.model.game.Tile;
import de.upb.cs.swtpra_04.qwirkle.model.game.TileIds;
import de.upb.swtpra1819interface.models.TileOnPosition;

/**
 * Handles UI representation of the game board
 */
public class BoardFragment extends Fragment implements BoardFragmentInterface {

    private OnBoardFragmentListener listener;

    // objects concerning view
    private GridLayout gridLayout_boardGrid;
    private ImageButton imageButton_zoomIn;
    private ImageButton imageButton_zoomOut;
    private ScrollView scrollView_boardVertical;
    private HorizontalScrollView scrollView_boardHorizontal;

    private float mZoom = (float) 0.0;


    // model objects
    private Board board;

    /**
     * Creates Layout and binds Views
     *
     * @param inflater           Layout inflator
     * @param container          parent ViewGroup
     * @param savedInstanceState data from old instance
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        scrollView_boardVertical = view.findViewById(R.id.scrollView_boardVertical);
        scrollView_boardHorizontal = view.findViewById(R.id.horizontalScrollView_boardHorizontal);

        imageButton_zoomIn = view.findViewById(R.id.imageButton_zoomPlus);
        imageButton_zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomIn();
            }
        });

        imageButton_zoomOut = view.findViewById(R.id.imageButton_zoomMinus);
        imageButton_zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomOut();
            }
        });

        //Defining Grid with a single starting cell
        gridLayout_boardGrid = (GridLayout) view.findViewById(R.id.gridLayout_boardGrid);
        gridLayout_boardGrid.setLayoutTransition(new LayoutTransition());
        gridLayout_boardGrid.removeAllViews();

        gridLayout_boardGrid.setColumnCount(1);
        gridLayout_boardGrid.setRowCount(1);

        return view;
    }


    /**
     * Called when a tile on the board has been clicked
     *
     * @param tile Pressed Tile
     */
    public void onClickedTile(BoardTile tile) {
        if (tile.getIsDummyTile()) {
            listener.onBoardTileClicked(tile);
        }
    }

    /**
     * Initialize board right after joining a game
     *
     * @param gameData game data reference for the board
     */
    public void setupOnJoin(GameData gameData) {
        this.board = new Board(this, gameData);
    }


    /**
     * Add a new tile on the board as specified within the tile
     *
     * @param newTile new tile to be placed on the board
     */
    public void addTileToGrid(BoardTile newTile) {
        if (listener != null) {
            ImageView iv;

            if (newTile.getView() == null) {
                // creating a new view for the tile
                iv = new ImageView(getContext());
                setViewParameters(iv);

                // setting view as attribute for the tile
                newTile.setView(iv);
            } else {
                iv = (ImageView) newTile.getView();
            }

            // set correct Drawable to image and configure color
            VectorChildFinder vector = new VectorChildFinder(getContext(),
                    TileIds.IDS[newTile.getShape()], iv);
            vector.findPathByName("color").setFillColor(
                    getContext().getResources().getColor(
                            TileIds.COLORS[newTile.getColor()]));
            iv.invalidate();

            // set the tile on the gridLayout_boardGrid as "new Tile" (uses view from the tile)
            newTile.setNewTile(true);
            setViewOnGridOf(newTile);

            //set onClickListener, set tag to its BoardTile as identifier
            iv.setTag(newTile);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BoardTile tile = (BoardTile) v.getTag();
                    onClickedTile(tile);
                }
            });
        }


    }

    private void setViewParameters(ImageView iv) {
        iv.setAdjustViewBounds(true);
        int px = Math.round(TypedValue.applyDimension( // Calculates current zoom to equivalent px
                TypedValue.COMPLEX_UNIT_DIP, (float) (Math.exp(mZoom) * 50),
                getResources().getDisplayMetrics()));
        iv.setMaxWidth(px); // uses px, NOT dp
        iv.setMinimumWidth(px);
        int padding = (int) (Math.exp(mZoom) * 10);
        iv.setPadding(padding, padding, padding, padding);
    }


    /**
     * Removes a tile from the board, tile has to be on the board.
     * No logic on whether removal is allowed
     *
     * @param tile Tile to be removed from the board
     */
    public void removeTileFromGrid(BoardTile tile) {
        if (gridLayout_boardGrid != null) {
            gridLayout_boardGrid.removeView(tile.getView());
        }
    }

    /**
     * Helper method which puts the tile on position saved within the tile
     * View has to be already set
     *
     * @param t tile to be set
     */
    private void setViewOnGridOf(BoardTile t) {
        // update view specifications
        GridLayout.Spec row = GridLayout.spec(t.getAbsY());
        GridLayout.Spec col = GridLayout.spec(t.getAbsX());

        // set view on the gridLayout_boardGrid with updated specifications
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gridLayout_boardGrid.addView(t.getView(), new GridLayout.LayoutParams(row, col));

            }
        });
    }


    /**
     * Extending gridLayout_boardGrid by adding rows and columns and when necessary moving the views on the gridLayout_boardGrid
     */
    public void addRowTop(List<BoardTile> tiles) {
        /* Create a new row on the bottom and move every view one cell down.*/
        gridLayout_boardGrid.setRowCount(gridLayout_boardGrid.getRowCount() + 1);
        gridLayout_boardGrid.removeAllViews();
        for (BoardTile t : tiles) {
            // increase position in y direction by one
            t.setAbsY(t.getAbsY() + 1);

            setViewOnGridOf(t);
        }
    }

    /**
     * Extending gridLayout_boardGrid by adding rows and columns and when necessary moving the views on the gridLayout_boardGrid
     */
    public void addRowBottom() {
        gridLayout_boardGrid.setRowCount(gridLayout_boardGrid.getRowCount() + 1);
    }

    /**
     * Extending gridLayout_boardGrid by adding rows and columns and when necessary moving the views on the gridLayout_boardGrid
     */
    public void addColumnRight() {
        gridLayout_boardGrid.setColumnCount(gridLayout_boardGrid.getColumnCount() + 1);
    }

    /**
     * Extending gridLayout_boardGrid by adding rows and columns and when necessary moving the views on the gridLayout_boardGrid
     */
    public void addColumnLeft(List<BoardTile> tiles) {
        /* Create a new column on the right and move every view one cell to the right.*/
        gridLayout_boardGrid.setColumnCount(gridLayout_boardGrid.getColumnCount() + 1);
        gridLayout_boardGrid.removeAllViews();
        for (BoardTile t : tiles) {
            // increase position in x direction by one
            t.setAbsX(t.getAbsX() + 1);

            setViewOnGridOf(t);
        }
    }

    public void layoutDummyTile(BoardTile tile) {
        addTileToGrid(tile);
    }

    public void removeDummyTiles() {
        if (board != null) {
            board.removeDummyTiles();
        }
    }


    public void showDummyTiles() {
        if (board != null) {
            board.layoutDummyTiles();
        }
    }

    /**
     * Adds new Tiles to the board
     *
     * @param update List of all new tiles
     */
    public void update(List<BoardTile> update) {
        board.removeDummyTiles();
        board.addTiles(update);
    }

    /**
     * Increases zoom by enlarging all tiles on the board
     */
    private void zoomIn() {
        if (mZoom < 1) {
            float oldZoom = mZoom;
            mZoom = (float) Math.round((mZoom + 0.1) * 10) / 10;
            updateViewSizeOnBoard(oldZoom);
        }
    }

    /**
     * Decreases zoom by shrinking all tiles on the board
     */
    private void zoomOut() {
        if (mZoom > -1.5) {
            float oldZoom = mZoom;
            mZoom = (float) Math.round((mZoom - 0.1) * 10) / 10;
            updateViewSizeOnBoard(oldZoom);
        }
    }

    /**
     * Updates all views placed on the board gridLayout_boardGrid with the current zoom
     * and moves scroll views to a new position to show approximately
     * the same spot on the board as before.
     * Zoom is calculated logarithmically.
     */
    private void updateViewSizeOnBoard(float oldZoom) {
        if (board != null) {
            List<BoardTile> tiles = board.getTilesOnBoard();
            List<BoardTile> dummyTiles = board.getDummyTiles();
            ImageView iv;

            for (BoardTile t : tiles) {
                iv = (ImageView) t.getView();
                setViewParameters(iv);
            }
            for (BoardTile t : dummyTiles) {
                iv = (ImageView) t.getView();
                setViewParameters(iv);
            }


            // get values to determine new position of scrollviews
            // variables ending with V are needed for the vertical scroll view, H for horizontal scroll view
            int oldTotalV = gridLayout_boardGrid.getHeight();
            int oldTotalH = gridLayout_boardGrid.getWidth();
            int posV = scrollView_boardVertical.getScrollY();
            int posH = scrollView_boardHorizontal.getScrollX();
            int padding = gridLayout_boardGrid.getPaddingBottom();  // padding is the same on all sides

            // the factor by which the view has changed
            float factor = (float) (Math.exp(mZoom) / Math.exp(oldZoom));

            // Calculate new size of the gridLayout_boardGrid. (The gridLayout_boardGrid doesn't yet know its new size, will get updated on drawing)
            // Padding doesn't change; only the tiles will get larger
            int newTotalV = Math.round((oldTotalV - 2 * padding) * factor + 2 * padding);
            int newTotalH = Math.round((oldTotalH - 2 * padding) * factor + 2 * padding);

            // Calculate new positions of the scroll views to stay approximately in the same spot
            int newPosV = (Integer) ((posV * newTotalV) / oldTotalV);
            int newPosH = (Integer) ((posH * newTotalH) / oldTotalH);

            // for vertical scroll view position in horizontal direction doesn't matter, same for horizontal scroll view
            scrollView_boardVertical.scrollTo(scrollView_boardVertical.getScrollX(), newPosV);
            scrollView_boardHorizontal.scrollTo(newPosH, scrollView_boardHorizontal.getScrollY());
        }
    }

    @Override
    public void showTile(BoardTile boardTile) {
        board.removeDummyTiles();
        board.addHoverTile(boardTile);
    }

    @Override
    public void clearTiles() {
        board.clearMove();
    }

    @Override
    public void playTiles() {

        board.playMove();
    }

    /**
     * Defines Method to parent know which tile on the board has been clicked
     */
    public interface OnBoardFragmentListener {
        void onBoardTileClicked(BoardTile tile);
    }

    /**
     * Attaches this fragment to the life cycle of the parent activity.
     * Android intern method.
     * Used here to make sure parent activity uses our interface.
     *
     * @param context parent activity
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBoardFragmentListener) {
            listener = (OnBoardFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement BoardFragment.OnBoardFragmentListener");
        }
    }

    /**
     * Detaches this fragment from parent activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    public List<TileOnPosition> getHoveringTiles() {
        return board.getHoveringTiles();
    }

    public List<Tile> getHoveringTilesAsTile() {
        return board.getHoveringTilesAsTile();
    }

}

