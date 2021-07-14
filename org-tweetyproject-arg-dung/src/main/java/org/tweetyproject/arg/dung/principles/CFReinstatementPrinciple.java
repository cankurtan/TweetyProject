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
 *  Copyright 2021 The TweetyProject Team <http://tweetyproject.org/contact/>
 */

package org.tweetyproject.arg.dung.principles;

import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;

import java.util.Collection;

/**
 * CF-Reinstatement Principle
 * A semantics satisfies cf-reinstatement if for all extensions E it holds that:
 * for all arguments a, if E u {a} is conflict-free and E defends a, then a is in E
 *
 * see: Baroni, P., & Giacomin, M. (2007). On principle-based evaluation of extension-based argumentation semantics.
 *
 * @author Lars Bengel
 */
public class CFReinstatementPrinciple extends Principle {
    @Override
    public String getName() {
        return "CF-Reinstatement";
    }

    @Override
    public boolean isApplicable(Collection<Argument> kb) {
        return (kb instanceof DungTheory);
    }


    @Override
    public boolean isSatisfied(Collection<Argument> kb, AbstractExtensionReasoner ev) {
        DungTheory theory = (DungTheory) kb;
        Collection<Extension> exts = ev.getModels(theory);

        for (Extension ext: exts) {
            for (Argument a: theory) {
                if (ext.contains(a)) {
                    continue;
                }

                // for all arguments a in theory \ E, iff E u {a} is conflict-free and E defends a, then cf-reinstatement is violated
                Extension extWithA = new Extension(ext);
                extWithA.add(a);
                if (theory.isConflictFree(extWithA) && theory.isAcceptable(a, ext)) {
                    return false;
                }
            }
        }
        return true;
    }
}