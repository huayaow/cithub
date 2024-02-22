package com.gist.cithub.backend.Repo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gist.cithub.backend.Repo.entity.ListEntity;
import com.gist.cithub.backend.Repo.entity.authorAndscholarEntity;
import com.gist.cithub.backend.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.gist.cithub.backend.Repo.dao.authorAndscholarDao;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("repo/author")
public class authorAndscholarController {

    @Autowired
    private com.gist.cithub.backend.Repo.service.authorAndinstitutionService authorAndinstitutionService;

    @Autowired
    private authorAndscholarDao authorAndscholarDao;

    @RequestMapping(value = "/testauthor", method = RequestMethod.POST)
    public R testAuthor() {
        System.out.println("testAuthortestAuthor");
        return R.ok();
    }

    @RequestMapping(value = "/listallauthor", method = RequestMethod.POST)
    public R listAllauthor(@RequestBody Map<String, Object> map) {
        String project = map.get("obj").toString();
        QueryWrapper<authorAndscholarEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT authorname").eq("projects", project);
        return R.ok().put("authors", authorAndinstitutionService.list(queryWrapper));
    }

    @RequestMapping(value = "/countTotalScholars", method = RequestMethod.POST)
    public R countTotalScholars(@RequestBody Map<String, Object> pageinfo) {
        String typeofPaper = (String) pageinfo.get("typerofPapers");
        QueryWrapper<authorAndscholarEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT authorname");
        long total = authorAndinstitutionService.count(queryWrapper);
        return R.ok().put("total", total);
    }

    @RequestMapping(value = "/listAllInstitutions", method = RequestMethod.POST)
    public R listAllInstitutions(@RequestBody Map<String, Object> map) {
        String project = map.get("obj").toString();
        QueryWrapper<authorAndscholarEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT institution").eq("projects", project).isNotNull("institution");
        ;
        return R.ok().put("Institutions", authorAndinstitutionService.list(queryWrapper));
    }

    @RequestMapping(value = "/countTotalInstitutions", method = RequestMethod.POST)
    public R countTotalInstitutions(@RequestBody Map<String, Object> pageinfo) {
        String typeofPaper = (String) pageinfo.get("typerofPapers");
        QueryWrapper<authorAndscholarEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT institution");
        long total = authorAndinstitutionService.count(queryWrapper);
        return R.ok().put("total", total);
    }

    @RequestMapping(value = "/countEachCountry", method = RequestMethod.POST)
    public R countEachCountry(@RequestBody Map<String, Object> info) {
        String typeOfPaper = (String) info.get("info");
        QueryWrapper<authorAndscholarEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("country", "COUNT(*) AS count")
                .isNotNull("country")
                .groupBy("country");
        System.out.println("authorService.listMaps(queryWrapper)" + authorAndinstitutionService.listMaps(queryWrapper));
        return R.ok().put("countEachCountry", authorAndinstitutionService.listMaps(queryWrapper));
    }

    @RequestMapping(value = "/ListAuthorsByCountry", method = RequestMethod.POST)
    public R ListAuthorsByCountry(@RequestBody Map<String, Object> info) {
//        System.out.println("info"+info);
        String country = (String) info.get("country");
        String typeOfPaper = (String) info.get("typeofPapers");
        QueryWrapper<authorAndscholarEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("country", country);
        queryWrapper.select("authorname");
//        System.out.println("authorService.list(queryWrapper)"+ authorService.listMaps(queryWrapper));
        List<String> list = authorAndinstitutionService.listMaps(queryWrapper).stream().map(map -> map.get("authorname").toString()).collect(Collectors.toList());

        return R.ok().put("AuthorList", list);
    }

    @RequestMapping(value = "/ListPapersByCountry", method = RequestMethod.POST)
    public R ListPapersByCountry(@RequestBody Map<String, Object> info) {
//        System.out.println("ListPapersByCountry info"+info);
        String country = (String) info.get("country");
        Map<String, Object> pageInfo = (Map<String, Object>) info.get("searchObj");
        Integer pagenum = (Integer) pageInfo.get("pagenum");
        Integer pagesize = (Integer) pageInfo.get("pagesize");
        String typerofPapers = (String) pageInfo.get("typerofPapers");
        QueryWrapper<authorAndscholarEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("country", country);
        queryWrapper.select("authorname");
//        System.out.println("authorService.list(queryWrapper)"+ authorService.listMaps(queryWrapper));
        List<String> listOfAuthor = authorAndinstitutionService.listMaps(queryWrapper).stream().map(map -> map.get("authorname").toString()).collect(Collectors.toList());
        List<ListEntity> ListPapersByCountryResult = authorAndinstitutionService.listPaperByCountry(pagenum, pagesize, typerofPapers, listOfAuthor);
        return R.ok().put("ListPapersByCountryResult", ListPapersByCountryResult);
    }

    @RequestMapping(value = "/CountDistributionScholars", method = RequestMethod.POST)
    public R CountDistributionScholars(@RequestBody Map<String, Object> info) {
        QueryWrapper<authorAndscholarEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("country", "COUNT(name) AS author_count");
        queryWrapper.groupBy("country");
        List<Map<String, Object>> result = authorAndscholarDao.selectMaps(queryWrapper);
        return R.ok().put("result",result);
    }


}
