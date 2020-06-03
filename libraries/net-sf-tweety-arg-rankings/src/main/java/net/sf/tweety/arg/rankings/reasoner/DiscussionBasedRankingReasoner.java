/*
 *  This file is part of "TweetyProject", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  TweetyProject is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *  Copyright 2018 The TweetyProject Team <http://tweetyproject.org/contact/>
 */
package net.sf.tweety.arg.rankings.reasoner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import net.sf.tweety.arg.rankings.semantics.LatticeArgumentRanking;
import net.sf.tweety.arg.rankings.semantics.NumericalArgumentRanking;
import net.sf.tweety.arg.rankings.util.RankingTools;

/**
 * This class implements the "Discussion-based" argument semantics approach as
 * proposed by [Amgoud, Ben-Naim. Ranking-based semantics for argumentation
 * frameworks. 2013]. It compares arguments by counting the number of paths
 * ending to to them. If some arguments are equivalent wrt. to their number of
 * direct attackers, the size of paths is increased recursively until a
 * difference is found.
 * 
 * @author Anna Gessler
 */
public class DiscussionBasedRankingReasoner extends AbstractRankingReasoner<LatticeArgumentRanking> {

	@Override
	public Collection<LatticeArgumentRanking> getModels(DungTheory bbase) {
		Collection<LatticeArgumentRanking> ranks = new HashSet<LatticeArgumentRanking>();
		ranks.add(this.getModel(bbase));
		return ranks;
	}

	@Override
	public LatticeArgumentRanking getModel(DungTheory kb) {
		int i_max = 6; // Treshold for maximum length of linear discussions (paths)

		Map<Argument, ArrayList<Double>> discussionCounts = new HashMap<Argument, ArrayList<Double>>();
		for (int i = 2; i <= i_max+1; i++) { //Start with paths of length i=2 (discussion_count for length 1 would be -1 for all arguments)
			for (Argument a : kb) {
				double discussion_count = getNumberOfPathsOfLength(kb, a, i);
				if ((i & 1) != 0)
					discussion_count = -discussion_count; // odd value => negative discussion count
				ArrayList<Double> argumentDiscussionCounts = discussionCounts.get(a);
				if (argumentDiscussionCounts == null)
					argumentDiscussionCounts = new ArrayList<Double>();
				argumentDiscussionCounts.add(discussion_count + 0.0);
				discussionCounts.put(a, argumentDiscussionCounts);
			}
		}

		LatticeArgumentRanking resultRanking = new LatticeArgumentRanking(kb.getNodes());
		for (Argument a : kb) {
			for (Argument b : kb) {
				Boolean args_equal = true;
				for (int i = 0; i < i_max && args_equal; i++) {
					NumericalArgumentRanking tempRanking = new NumericalArgumentRanking();
					tempRanking.put(a, discussionCounts.get(a).get(i));
					tempRanking.put(b, discussionCounts.get(b).get(i));
					if (tempRanking.isStrictlyLessAcceptableThan(a, b)) {
						resultRanking.setStrictlyLessOrEquallyAcceptableThan(a, b);
						args_equal = false;
					} else if (tempRanking.isStrictlyLessAcceptableThan(b, a)) {
						resultRanking.setStrictlyLessOrEquallyAcceptableThan(b, a);
						args_equal = false;
					}
				}
				if (args_equal) {
					resultRanking.setStrictlyLessOrEquallyAcceptableThan(b, a);
					resultRanking.setStrictlyLessOrEquallyAcceptableThan(a, b);
				}
			}
		
		}

		return resultRanking;
	}

	/**
	 * Returns the number of linear discussions of the given length in the given
	 * DungTheory for the given argument.
	 * 
	 * @param base the abstract argumentation framework
	 * @param a    an argument
	 * @param i    length of linear discussions
	 * @return the number of linear discussions of the given length
	 */
	public int getNumberOfPathsOfLength(DungTheory base, Argument a, int i) {
		if (i == 0 || i == 1)
			return i;

		HashSet<ArrayList<Argument>> paths = new HashSet<ArrayList<Argument>>();

		// add linear discussions of length 2
		for (Argument attacker : base.getAttackers(a)) {
			ArrayList<Argument> path = new ArrayList<Argument>();
			path.add(a);
			path.add(attacker);
			paths.add(path);
		}

		int j = 2;
		while (j < i && !paths.isEmpty()) {
			paths = RankingTools.getPathsOfHigherSize(paths, base); // recursively add linear discussions of length>2
			j++;
		}
		return paths.size();
	}

}
