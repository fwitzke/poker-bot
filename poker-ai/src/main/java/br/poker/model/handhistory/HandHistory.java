package br.poker.model.handhistory;

import java.util.ArrayList;
import java.util.List;

import br.poker.model.action.Action;
import br.poker.util.Logger;

public class HandHistory {
	private List<Entry> entries;

	public HandHistory() {
		entries = new ArrayList<Entry>();
	}

	public Entry get(int index) {
		return entries.get(index);
	}

	public void addEntry(Entry entry) {
		Logger.log(entry.toString());
		entries.add(entry);
	}

	public Integer size() {
		return entries.size();
	}

	public boolean contains(Entry entry) {
		return entries.contains(entry);
	}

	public void clear() {
		entries.clear();
	}

	public boolean hasSmallBlind() {
		for (Entry e : entries)
			if(e.getAction().equals(Action.POST_SB))
				return true;
		return false;
	}

	public boolean hasBigBlind() {
		for (Entry e : entries)
			if(e.getAction().equals(Action.POST_BB))
				return true;
		return false;
	}
}
