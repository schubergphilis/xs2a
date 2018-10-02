/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.aspsp.aspspmockserver;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.Before;
import org.junit.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ModuleDependencyTest {

    private JavaClasses classes;

    @Before
    public void setUp() throws Exception {
        classes = new ClassFileImporter().importPackages("de.adorsys.aspsp.aspspmockserver");
    }

    @Test
    public void noConsentApiDependency() {

        ArchRule myRule = noClasses().that().resideInAPackage("..aspspmockserver..")
                .should().dependOnClassesThat().resideInAPackage("..consent.api..");

        myRule.check(classes);
    }

    @Test
    public void noSpiApiDependency() {

        ArchRule myRule = noClasses().that().resideInAPackage("..aspspmockserver..")
                .should().dependOnClassesThat().resideInAPackage("..xs2a.spi..");

        myRule.check(classes);
    }
}
