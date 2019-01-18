package com.mxcg.common.util.date;

public class NumberItem
{
    private int index; //第几个数字块

    private String content; //数字块内容

    private NumberDateUnit unit; //是否有明确的单位

    //private Boolean pm; //是否下午

    private int guessindex; //大文本已经拆分到何处

    public NumberItem(int index, String content)
    {
        this.index = index;
        this.content = content;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public NumberDateUnit getUnit()
    {
        return unit;
    }

    public void setUnit(NumberDateUnit unit)
    {
        this.unit = unit;
    }

//    public Boolean isPm()
//    {
//        return pm;
//    }
//    
//    public void setPm(Boolean pm)
//    {
//        this.pm = pm;
//    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(content);
        return builder.toString();
    }

    public void reset()
    {
        guessindex = 0;
    }

    public boolean isguessfinished()
    {
        return guessindex >= content.length();
    }

    public void guess(int afterindex, String[] s, Boolean isPM)
    {
        //只计算后面段的数字
        if (index <= afterindex)
            guessindex = 10000000;
        if (isguessfinished())
            return;
        //小于等于两个字符的整个使用，不拆分
        if (content.length() <= 2)
        {
            fillallnumber(s,isPM);
        }
        else
        {
            //填年月日时分秒
            if (s[0] == null)
            {
                guessindex = fillnumberyear(guessindex, s, 0);
                guessindex = fillnumber(guessindex, s, 1, 12);
                guessindex = fillnumber(guessindex, s, 2, 31);
                guessindex = fillnumber(guessindex, s, 3, 23);
                guessindex = fillnumber(guessindex, s, 4, 59);
                guessindex = fillnumber(guessindex, s, 5, 59);
            }
            //填月日时分秒
            else if (s[1] == null)
            {
                guessindex = fillnumber(guessindex, s, 1, 12);
                guessindex = fillnumber(guessindex, s, 2, 31);
                guessindex = fillnumber(guessindex, s, 3, 23);
                guessindex = fillnumber(guessindex, s, 4, 59);
                guessindex = fillnumber(guessindex, s, 5, 59);
            }
            //填日时分秒
            else if (s[2] == null)
            {
                guessindex = fillnumber(guessindex, s, 2, 31);
                guessindex = fillnumber(guessindex, s, 3, 23);
                guessindex = fillnumber(guessindex, s, 4, 59);
                guessindex = fillnumber(guessindex, s, 5, 59);
            }
            //填时分秒
            else if (s[3] == null)
            {
                guessindex = fillnumber(guessindex, s, 3, 23);
                if(s[3] != null)
                {
                    int h = Integer.parseInt(s[3]);
                    if(isPM == null)
                    {
                        s[3] = content;
                    }
                    else if(isPM)
                    {
                        if(h!=12) s[3] =  String.valueOf( h + 12);
                    }
                    else
                    {
                        if(h==12) s[3] = "0";
                    }
                }
                guessindex = fillnumber(guessindex, s, 4, 59);
                guessindex = fillnumber(guessindex, s, 5, 59);
            }
            //填分秒
            else if (s[4] == null)
            {

                guessindex = fillnumber(guessindex, s, 4, 59);
                guessindex = fillnumber(guessindex, s, 5, 59);
            }
            //填秒
            else if (s[5] == null)
            {
                guessindex = fillnumber(guessindex, s, 5, 59);
            }
        }
    }

    private void fillallnumber(String[] s, Boolean isPM)
    {
        if (s[0] == null)
            s[0] = content;
        else if (s[1] == null)
            s[1] = content;
        else if (s[2] == null)
            s[2] = content;
        else if (s[3] == null)
        {
            int h = Integer.parseInt(content);
            if(isPM == null)
            {
                s[3] = content;
            }
            else if(isPM)
            {
                if(h!=12)
                    s[3] =  String.valueOf( h + 12);
                else
                    s[3] = content;
            }
            else
            {
                if(h==12)
                    s[3] = "0";
                else
                    s[3] = content;
            }
        }
        else if (s[4] == null)
            s[4] = content;
        else if (s[5] == null)
            s[5] = content;
        guessindex = 10000000;
    }

    private Integer getnumber(int start, int length)
    {
        if (content.length() < start + length)
            return null;
        return Integer.parseInt(content.substring(start, start + length));
    }

    private Integer fillnumber(int startindex, String[] s, int stringindex, int max)
    {
        Integer number = getnumber(startindex, 2);
        if (number == null || number > max)
        {
            number = getnumber(startindex, 1);
            if (number != null)
                s[stringindex] = number.toString();
            startindex = startindex + 1;
        }
        else
        {
            startindex = startindex + 2;
            s[stringindex] = number.toString();
        }
        return startindex;
    }

    private Integer fillnumberyear(int startindex, String[] s, int stringindex)
    {
        Integer year = getnumber(startindex, 2);
        if (year == null)
        {
            startindex = startindex + 2;
        }
        else if (year == 19 || year == 20)
        {
            Integer year4 = getnumber(startindex, 4);
            if (year4 == null)
            {
                s[stringindex] = year.toString();
                startindex = startindex + 2;
            }
            else
            {
                s[stringindex] = year4.toString();
                startindex = startindex + 4;
            }
        }
        else
        {
            s[stringindex] = year.toString();
            startindex = startindex + 2;
        }
        return startindex;
    }
}
