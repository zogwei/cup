/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.aben.cup.jmx.modeler.modules;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.management.ObjectName;

//import org.apache.commons.digester3.Digester;

import com.aben.cup.digester.Digester;
import com.aben.cup.jmx.modeler.ManagedBean;
import com.aben.cup.jmx.modeler.Registry;
import com.aben.cup.log.logging.Log;
import com.aben.cup.log.logging.LogFactory;

public class MbeansDescriptorsDigesterSource extends ModelerSource
{
    private static final Log log =
            LogFactory.getLog(MbeansDescriptorsDigesterSource.class);

    Registry registry;
    String type;
    List<ObjectName> mbeans = new ArrayList<ObjectName>();
    protected static volatile Digester digester = null;
    
    protected static Digester createDigester() {

        Digester digester = new Digester();
        digester.setNamespaceAware(false);
        digester.setValidating(false);
        URL url = Registry.getRegistry(null, null).getClass().getResource
            ("/com/cup/jmx/modeler/mbeans-descriptors.dtd");
        digester.register
            ("-//Apache Software Foundation//DTD Model MBeans Configuration File",
                url.toString());
        
        // Configure the parsing rules
        digester.addObjectCreate
            ("mbeans-descriptors/mbean",
            "com.cup.jmx.modeler.ManagedBean");
        digester.addSetProperties
            ("mbeans-descriptors/mbean");
        digester.addSetNext
            ("mbeans-descriptors/mbean",
                "add",
            "java.lang.Object");
        
        digester.addObjectCreate
            ("mbeans-descriptors/mbean/attribute",
            "com.cup.jmx.modeler.AttributeInfo");
        digester.addSetProperties
            ("mbeans-descriptors/mbean/attribute");
        digester.addSetNext
            ("mbeans-descriptors/mbean/attribute",
                "addAttribute",
            "com.cup.jmx.modeler.AttributeInfo");
        
        /*digester.addObjectCreate
            ("mbeans-descriptors/mbean/attribute/descriptor/field",
            "com.cup.jmx.modeler.FieldInfo");
        digester.addSetProperties
            ("mbeans-descriptors/mbean/attribute/descriptor/field");
        digester.addSetNext
            ("mbeans-descriptors/mbean/attribute/descriptor/field",
                "addField",
            "com.cup.jmx.modeler.FieldInfo");
        
        digester.addObjectCreate
            ("mbeans-descriptors/mbean/constructor",
            "com.cup.jmx.modeler.ConstructorInfo");
        digester.addSetProperties
            ("mbeans-descriptors/mbean/constructor");
        digester.addSetNext
            ("mbeans-descriptors/mbean/constructor",
                "addConstructor",
            "com.cup.jmx.modeler.ConstructorInfo");
        
        digester.addObjectCreate
            ("mbeans-descriptors/mbean/constructor/descriptor/field",
            "com.cup.jmx.modeler.FieldInfo");
        digester.addSetProperties
            ("mbeans-descriptors/mbean/constructor/descriptor/field");
        digester.addSetNext
            ("mbeans-descriptors/mbean/constructor/descriptor/field",
                "addField",
            "com.cup.jmx.modeler.FieldInfo");
        
        digester.addObjectCreate
            ("mbeans-descriptors/mbean/constructor/parameter",
            "com.cup.jmx.modeler.ParameterInfo");
        digester.addSetProperties
            ("mbeans-descriptors/mbean/constructor/parameter");
        digester.addSetNext
            ("mbeans-descriptors/mbean/constructor/parameter",
                "addParameter",
            "com.cup.jmx.modeler.ParameterInfo");
        
        digester.addObjectCreate
            ("mbeans-descriptors/mbean/descriptor/field",
            "com.cup.jmx.modeler.FieldInfo");
        digester.addSetProperties
            ("mbeans-descriptors/mbean/descriptor/field");
        digester.addSetNext
            ("mbeans-descriptors/mbean/descriptor/field",
                "addField",
            "com.cup.jmx.modeler.FieldInfo");
        */
        digester.addObjectCreate
            ("mbeans-descriptors/mbean/notification",
            "com.cup.jmx.modeler.NotificationInfo");
        digester.addSetProperties
            ("mbeans-descriptors/mbean/notification");
        digester.addSetNext
            ("mbeans-descriptors/mbean/notification",
                "addNotification",
            "com.cup.jmx.modeler.NotificationInfo");
        
        digester.addObjectCreate
            ("mbeans-descriptors/mbean/notification/descriptor/field",
            "com.cup.jmx.modeler.FieldInfo");
        digester.addSetProperties
            ("mbeans-descriptors/mbean/notification/descriptor/field");
        digester.addSetNext
            ("mbeans-descriptors/mbean/notification/descriptor/field",
                "addField",
            "com.cup.jmx.modeler.FieldInfo");
        
        digester.addCallMethod
            ("mbeans-descriptors/mbean/notification/notification-type",
                "addNotifType", 0);
        
        digester.addObjectCreate
            ("mbeans-descriptors/mbean/operation",
            "com.cup.jmx.modeler.OperationInfo");
        digester.addSetProperties
            ("mbeans-descriptors/mbean/operation");
        digester.addSetNext
            ("mbeans-descriptors/mbean/operation",
                "addOperation",
            "com.cup.jmx.modeler.OperationInfo");
        
        digester.addObjectCreate
            ("mbeans-descriptors/mbean/operation/descriptor/field",
            "com.cup.jmx.modeler.FieldInfo");
        digester.addSetProperties
            ("mbeans-descriptors/mbean/operation/descriptor/field");
        digester.addSetNext
            ("mbeans-descriptors/mbean/operation/descriptor/field",
                "addField",
            "com.cup.jmx.modeler.FieldInfo");
        
        digester.addObjectCreate
            ("mbeans-descriptors/mbean/operation/parameter",
            "com.cup.jmx.modeler.ParameterInfo");
        digester.addSetProperties
            ("mbeans-descriptors/mbean/operation/parameter");
        digester.addSetNext
            ("mbeans-descriptors/mbean/operation/parameter",
                "addParameter",
            "com.cup.jmx.modeler.ParameterInfo");
        
        return digester;
        
    }
    
    public void setRegistry(Registry reg) {
        this.registry=reg;
    }


    /**
     * @deprecated Unused. Will be removed in Tomcat 8.0.x
     */
    @Deprecated
    public void setLocation( String loc ) {
        this.location=loc;
    }

    /** Used if a single component is loaded
     *
     * @param type
     */
    public void setType( String type ) {
       this.type=type;
    }

    public void setSource( Object source ) {
        this.source=source;
    }

    @Override
    public List<ObjectName> loadDescriptors( Registry registry, String type,
            Object source) throws Exception {
        setRegistry(registry);
        setType(type);
        setSource(source);
        execute();
        return mbeans;
    }

    public void execute() throws Exception {
        if (registry == null) {
            registry = Registry.getRegistry(null, null);
        }

        InputStream stream = (InputStream) source;

        if (digester == null) {
            digester = createDigester();
        }
        ArrayList<ManagedBean> loadedMbeans = new ArrayList<ManagedBean>();
        
        synchronized (digester) {
            
            // Process the input file to configure our registry
            try {
                // Push our registry object onto the stack
                digester.push(loadedMbeans);
                digester.parse(stream);
            } catch (Exception e) {
                log.error("Error digesting Registry data", e);
                throw e;
            } finally {
                digester.reset();
//                digester.resetRoot();
//                digester.setErrorHandler(null);
//                digester.clear();
            }
        
        }
        Iterator<ManagedBean> iter = loadedMbeans.iterator();
        while (iter.hasNext()) {
            registry.addManagedBean(iter.next());
        }
    }
}
