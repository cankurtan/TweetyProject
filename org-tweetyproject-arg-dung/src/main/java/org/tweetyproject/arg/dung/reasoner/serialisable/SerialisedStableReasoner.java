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
 *  Copyright 2022 The TweetyProject Team <http://tweetyproject.org/contact/>
 */
package org.tweetyproject.arg.dung.reasoner.serialisable;

import org.tweetyproject.arg.dung.syntax.TransitionState;


/**
 * Serialised version of the stable semantics
 *
 * @author Lars Bengel
 */
public class SerialisedStableReasoner extends SerialisedAdmissibleReasoner {
    /**
     * A set S is accepted iff the AF of the state is empty
     * @param state the current state
     * @return true, iff the AF has  no arguments or attacks
     */
    @Override
    public boolean terminationFunction(TransitionState state) {
        return state.getTheory().isEmpty();
    }
}
