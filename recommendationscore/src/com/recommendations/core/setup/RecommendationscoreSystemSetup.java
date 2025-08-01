/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.recommendations.core.setup;



import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import com.recommendations.core.constants.RecommendationscoreConstants;


@SystemSetup(extension = RecommendationscoreConstants.EXTENSIONNAME)
public class RecommendationscoreSystemSetup
{

	@SystemSetup(process = SystemSetup.Process.ALL, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{

	}

	private InputStream getImageStream()
	{
		return RecommendationscoreSystemSetup.class.getResourceAsStream("/recommendationscore/sap-hybris-platform.png");
	}
}
