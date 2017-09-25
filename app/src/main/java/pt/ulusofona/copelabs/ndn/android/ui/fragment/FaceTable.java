/*
 * @version 1.0
 * COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 2017-mm-dd
 * Fragment for displaying the FaceTable
 * @author Seweryn Dynerowicz (COPELABS/ULHT)
 */

package pt.ulusofona.copelabs.ndn.android.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import pt.ulusofona.copelabs.ndn.R;
import pt.ulusofona.copelabs.ndn.android.models.Face;
import pt.ulusofona.copelabs.ndn.android.umobile.OpportunisticDaemon;
import pt.ulusofona.copelabs.ndn.databinding.FragmentTableDbBinding;
import pt.ulusofona.copelabs.ndn.databinding.ItemFaceBinding;

/** Fragment used to display the FaceTable of the running daemon. */
public class FaceTable extends Fragment implements Refreshable {
	private FragmentTableDbBinding mTableBinding;
	private List<Face> mFaceTable = new ArrayList<>();
	private ArrayAdapter<Face> mFaceTableAdapter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTableBinding = FragmentTableDbBinding.inflate(getActivity().getLayoutInflater());
		mTableBinding.title.setText(R.string.facetable);
		mFaceTableAdapter = new FaceTableAdapter(getContext(), R.layout.item_face);
		mTableBinding.contents.setAdapter(mFaceTableAdapter);
	}

	/** Fragment lifecycle method. See https://developer.android.com/guide/components/fragments.html
	 * @param inflater Android-provided layout inflater
	 * @param parent parent View within the hierarchy
	 * @param savedInstanceState previously saved state of the View instance
	 * @return the View to be used
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		return mTableBinding.getRoot();
	}

	/** Performs a refresh of the contents of the enclosed table
	 * @param daemon Binder to the ForwardingDaemon used to retrieve the new entries to update this View with
	 */
	public void refresh(@NonNull OpportunisticDaemon.Binder daemon) {
		mFaceTable.clear();
		mFaceTable.addAll(daemon.getFaceTable());
		mFaceTableAdapter.clear();
		mFaceTableAdapter.addAll(mFaceTable);
	}

	/** Clear the contents of the enclosed table */
	public void clear() {
		mFaceTable.clear();
	}

	private class FaceTableAdapter extends ArrayAdapter<Face> {
		private LayoutInflater mInflater;

		public FaceTableAdapter(@NonNull Context context, @LayoutRes int resource) {
			super(context, resource);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@NonNull
		@Override
		public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			ItemFaceBinding ifb = ItemFaceBinding.inflate(mInflater, parent, false);
			ifb.setFace(mFaceTable.get(position));
			return ifb.getRoot();
		}
	}
}