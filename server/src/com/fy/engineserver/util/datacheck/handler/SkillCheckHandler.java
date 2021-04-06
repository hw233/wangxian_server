package com.fy.engineserver.util.datacheck.handler;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.activeskills.CommonAttackSkill;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.datacheck.DataCheckHandler;
import com.fy.engineserver.util.datacheck.SendHtmlToMail;

public class SkillCheckHandler implements DataCheckHandler {

	@Override
	public String getHandlerName() {
		return "技能检查";
	}

	@Override
	public String[] involveConfigfiles() {
		return new String[] { "skills.xls" };
	}

	@Override
	/**
	 * 技能|id|描述
	 */
	public CompoundReturn getCheckResult() {
		CompoundReturn cr = CompoundReturn.create();
		String[] titles = new String[] { "技能", "技能id", "描述" };
		List<SendHtmlToMail> mailList = new ArrayList<SendHtmlToMail>();

		CareerManager cm = CareerManager.getInstance();

		CommonAttackSkill[] commonAttackSkills = cm.getCommonAttackSkills();
		Skill[] monsterSkills = cm.getMonsterSkills();
		for (CommonAttackSkill cas : commonAttackSkills) {
			int id = cas.getId();
			int i = -1;
			for (CommonAttackSkill cas2 : commonAttackSkills) {
				if (cas2.getId() == id) {
					i++;
				}
			}
			for (Skill s : monsterSkills) {
				if (s.getId() == id) {
					i++;
				}
			}
			if (i > 0) {
				mailList.add(new SendHtmlToMail(titles, new String[] { "技能", id + "", "技能<font color=red>[" + cas.getName() + "]</font>id重复" + i + "次" }));
			}
		}
		for (Skill s : monsterSkills) {
			int id = s.getId();
			int i = -1;
			for (CommonAttackSkill cas2 : commonAttackSkills) {
				if (cas2.getId() == id) {
					i++;
				}
			}
			for (Skill s2 : monsterSkills) {
				if (s2.getId() == id) {
					i++;
				}
			}
			if (i > 0) {
				mailList.add(new SendHtmlToMail(titles, new String[] { "技能", id + "", "技能<font color=red>[" + s.getName() + "]</font>id重复" + i + "次" }));
			}
		}

		return cr.setBooleanValue(mailList.size() > 0).setObjValue(mailList.toArray(new SendHtmlToMail[0]));
	}
}
