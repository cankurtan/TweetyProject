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
 *  Copyright 2019 The TweetyProject Team <http://tweetyproject.org/contact/>
 */
package net.sf.tweety.arg.rankings.postulates;

import java.util.Collection;
import java.util.Iterator;

import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import net.sf.tweety.arg.rankings.reasoner.AbstractRankingReasoner;
import net.sf.tweety.arg.rankings.semantics.ArgumentRanking;

/**
 *  The "self-contradiction" postulate for ranking semantics as proposed 
 *  in [Matt, Toni. A game-theoretic measure of argument strength
 *  for abstract argumentation. 2008]: 
 *  A self-attacking argument is ranked lower than any 
 *  non-self-attacking argument.
 * 
 * @author Anna Gessler
 *
 */
public class RaSelfContradiction extends RankingPostulate {

	@Override
	public String getName() {
		return "Self-Contradiction";
	}

	@Override
	public boolean isApplicable(Collection<Argument> kb) {
		return (kb instanceof DungTheory && kb.size()>=2);
		
	}

	@Override
	public boolean isSatisfied(Collection<Argument> kb, AbstractRankingReasoner<ArgumentRanking> ev) {
		if (!this.isApplicable(kb))
			return true;
		DungTheory dt = new DungTheory((DungTheory) kb);
		Iterator<Argument> it = dt.iterator();
		Argument a = it.next();
		Argument b = it.next();
		
		ArgumentRanking ranking = ev.getModel((DungTheory)dt);
		if (ranking.isIncomparable(a, b)) {
			if (IGNORE_INCOMPARABLE_ARGUMENTS)
				return true;
			else
				return false;
		}
		if (dt.isAttackedBy(a, a) && !dt.isAttackedBy(b, b))
			return ranking.isStrictlyLessAcceptableThan(a, b);
		return true;
		
	}

	

}
