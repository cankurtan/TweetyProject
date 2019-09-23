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

import java.util.Collection;
import java.util.HashSet;

import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import net.sf.tweety.arg.rankings.semantics.NumericalArgumentRanking;
import net.sf.tweety.arg.social.reasoner.IssReasoner;
import net.sf.tweety.arg.social.semantics.SimpleProductSemantics;
import net.sf.tweety.arg.social.semantics.SocialMapping;
import net.sf.tweety.arg.social.syntax.SocialAbstractArgumentationFramework;

/**
 * This class implements the ranking-based "SAF" semantics approach as proposed
 * by [Bonzon, Delobelle, Konieczny, Maudet. A Comparative Study of Ranking-Based 
 * Semantics for Abstract Argumentation. AAAI 2016]. It uses social abstract argumentation 
 * frameworks and the simple product semantic which were introduced by 
 * [Leite, Martins. Social abstract argumentation. IJCAI 2011].
 * 
 * @author Anna Gessler
 */
public class SAFRankingReasoner extends AbstractRankingReasoner<NumericalArgumentRanking> {

	@Override
	public Collection<NumericalArgumentRanking> getModels(DungTheory bbase) {
		Collection<NumericalArgumentRanking> ranks = new HashSet<NumericalArgumentRanking>();
		ranks.add(this.getModel(bbase));
		return ranks;
	}

	@Override
	public NumericalArgumentRanking getModel(DungTheory kb) {
		SocialAbstractArgumentationFramework saf = new SocialAbstractArgumentationFramework();
		saf.add(kb);
		for (Argument a : kb)
			saf.voteUp(a, 1);
		IssReasoner reasoner6 = new IssReasoner(new SimpleProductSemantics(0.1), 0.001);
		SocialMapping<Double> result = reasoner6.getModel(saf);
		NumericalArgumentRanking ranking = new NumericalArgumentRanking();
		for (Argument a : kb)
			ranking.put(a, result.get(a));
		return ranking;
	}

}
