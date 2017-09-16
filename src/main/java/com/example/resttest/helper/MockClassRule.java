package com.example.resttest.helper;

import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Finn on 16-09-2017.
 */
public class MockClassRule implements TestRule, MethodRule
{
    // variables declaration
    private static int proxyPort;

    // constructor
    public MockClassRule (int proxyPort)
    {
        try
        {
            MockHelper.setOptions(proxyPort);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public MockClassRule (int mockPort, int proxyPort)
    {
        try
        {
            MockHelper.setOptions(mockPort, proxyPort);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public MockClassRule (int mockPort, String proxyHost, int proxyPort)
    {
        try
        {
            MockHelper.setOptions(mockPort, proxyHost, proxyPort);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public MockClassRule (int mockPort, String proxyHost)
    {
        try
        {
            MockHelper.setOptions(mockPort, proxyHost);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // methods
    @Override
    public Statement apply(Statement statement, Description description)
    {
        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                MockHelper.mockServerStart();

                statement.evaluate();

                MockHelper.mockServerStop();
            }
        };
    }

    @Override
    public Statement apply(Statement statement, FrameworkMethod frameworkMethod, Object o)
    {
        return this.apply(statement, Description.EMPTY);
    }
}
