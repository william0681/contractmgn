package com.zx.serviceschedule.service;

import com.zx.serviceschedule.mapper.AccountReceivableDAO;
import com.zx.serviceschedule.mapper.CommissionDAO;
import com.zx.serviceschedule.model.AccountReceivable;
import com.zx.serviceschedule.model.AccountReceivableExample;
import com.zx.serviceschedule.model.Commission;
import com.zx.serviceschedule.model.CommissionExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
@Configuration
public class ReceivableScheduleTask {

    @Autowired
    AccountReceivableDAO accountReceivableDAO;
    @Autowired
    CommissionDAO commissionDAO;

    @Scheduled(cron = "0 1 0 * * ? ")
    public void checkoutReceivableStatus() {
        Date nowDate = new Date();
        AccountReceivableExample example = new AccountReceivableExample();
        example.or().andStatusNotEqualTo(5);
        List<AccountReceivable> accountReceivables =accountReceivableDAO.selectByExample(example);
        for(AccountReceivable accountReceivable : accountReceivables){
            if(accountReceivable.getId()!=1&&accountReceivable.getId()!=2) {
                Date startDate = accountReceivable.getStartDate();
                Date endDate = accountReceivable.getEndDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                endDate = calendar.getTime();
                if (nowDate.after(startDate) && nowDate.before(endDate)) {
                    accountReceivable.setStatus(2);
                    accountReceivable.setDays(differentDays(nowDate, endDate));
                }
                if (nowDate.after(endDate)) {
                    accountReceivable.setStatus(1);
                    accountReceivable.setDays(differentDays(endDate, nowDate));
                }
                accountReceivableDAO.updateByPrimaryKey(accountReceivable);
            }
        }
    }

    @Scheduled(cron = "0 1 0 * * ? ")
    public void checkCommissionStatus(){

        Date nowDate = new Date();
        CommissionExample example = new CommissionExample();
        example.or().andStatusNotEqualTo(4);
        List<Commission> commissions = commissionDAO.selectByExample(example);
        for(Commission commission : commissions){
            Date dueDate = commission.getDueDate();
            if(dueDate.after(nowDate)){
                int days = differentDays(nowDate,dueDate);
                if(days<=10){
                    commission.setStatus(2);
                    commission.setDays(days);
                }else{
                    commission.setStatus(3);
                }
            }else{
                commission.setStatus(1);
                commission.setDays(differentDays(dueDate,nowDate));
            }
            commissionDAO.updateByPrimaryKey(commission);
        }

    }



    public int differentDays(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2-day1) ;
        }
        else    //不同年
        {
            return day2-day1;
        }
    }
}
