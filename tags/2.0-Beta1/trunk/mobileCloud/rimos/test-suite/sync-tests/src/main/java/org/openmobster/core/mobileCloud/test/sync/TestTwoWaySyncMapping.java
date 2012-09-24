/**
 * Copyright (c) {2003,2010} {openmobster@gmail.com} {individual contributors as indicated by the @authors tag}.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openmobster.core.mobileCloud.test.sync;

import org.openmobster.core.mobileCloud.rimos.module.mobileObject.MobileObject;
import org.openmobster.core.mobileCloud.rimos.module.sync.*;

/**
 * 
 * @author openmobster@gmail.com
 *
 */
public final class TestTwoWaySyncMapping extends AbstractMapSyncTest
{		
	/**
	 * 
	 */
	public void runTest()
	{
		try
		{
			
			//Add test case			
			this.setUp("add");
			SyncService.getInstance().performTwoWaySync(service, service, false);					
			this.assertRecordPresence("unique-1-luid", this.getInfo()+"/add");
			this.assertRecordPresence("unique-2-luid", this.getInfo()+"/add");
			this.assertRecordPresence("unique-3-luid", this.getInfo()+"/add");
			this.assertRecordPresence("unique-4-luid", this.getInfo()+"/add");
			this.assertRecordAbsence("unique-1", this.getInfo()+"/add");
			this.assertRecordAbsence("unique-2", this.getInfo()+"/add");
			this.assertRecordAbsence("unique-3", this.getInfo()+"/add");
			this.assertRecordAbsence("unique-4", this.getInfo()+"/add");
			this.tearDown();
			
			
			//Replace test case			
			this.setUp("replace");
			SyncService.getInstance().performTwoWaySync(service, service, false);						
			this.assertRecordAbsence("unique-1", this.getInfo()+"/replace");
			this.assertRecordAbsence("unique-2", this.getInfo()+"/replace");
			this.assertRecordPresence("unique-1-luid", this.getInfo()+"/replace");
			this.assertRecordPresence("unique-2-luid", this.getInfo()+"/replace");			
			MobileObject afterUnique1 = this.getRecord("unique-1-luid");
			MobileObject afterUnique2 = this.getRecord("unique-2-luid");
			this.assertEquals(afterUnique1.getValue("message"), 
			"<tag apos='apos' quote=\"quote\" ampersand='&'>unique-1/Updated/Server</tag>", 
			this.getInfo()+"/replace/updated/unique-1");
			this.assertEquals(afterUnique2.getValue("message"), 
			"<tag apos='apos' quote=\"quote\" ampersand='&'>unique-2/Updated/Client</tag>", 
			this.getInfo()+"/replace/updated/unique-2");
			this.tearDown();
			
			
			//Run the Delete test case.
			this.setUp("delete");
			SyncService.getInstance().performTwoWaySync(service, service, false);				
			this.assertRecordAbsence("unique-1-luid", this.getInfo()+"/delete");
			this.assertRecordAbsence("unique-2-luid", this.getInfo()+"/delete");
			this.tearDown();
			
			//Conflict test case
			this.setUp("conflict");
			SyncService.getInstance().performTwoWaySync(service, service, false);
			MobileObject afterUnique1Conflict = this.getRecord("unique-1-luid");
			MobileObject afterUnique2Conflict = this.getRecord("unique-2-luid");			
			this.assertRecordPresence("unique-1-luid", this.getInfo()+"/conflict");
			this.assertRecordPresence("unique-2-luid", this.getInfo()+"/conflict");
			this.assertRecordAbsence("unique-1", this.getInfo()+"/conflict");
			this.assertRecordAbsence("unique-2", this.getInfo()+"/conflict");
			this.assertEquals(afterUnique1Conflict.getValue("message"), 
			"<tag apos='apos' quote=\"quote\" ampersand='&'>unique-1/Updated/Client</tag>", 
			this.getInfo()+"/conflict/updated/unique-1");
			this.assertEquals(afterUnique2Conflict.getValue("message"), 
			"<tag apos='apos' quote=\"quote\" ampersand='&'>2/Message</tag>", 
			this.getInfo()+"/conflict/updated/unique-2");
			this.tearDown();
		}
		catch(Exception e)
		{
			throw new RuntimeException(e.toString());
		}
	}	
}